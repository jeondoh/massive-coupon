import http from 'k6/http';
import {check, group, sleep} from 'k6';
import {Counter, Rate} from 'k6/metrics';
import {getToken} from "./auth.js";

const requestCounter = new Counter('http_requests_total');
const requestFailureRate = new Rate('http_request_failure_rate');
const queueRedirectRate = new Rate('queue_redirect_rate');
const directIssueRate = new Rate('direct_issue_rate');
const couponIssueSuccessRate = new Rate('coupon_issue_success_rate');

export const options = {
    // 단계별 부하 테스트
    /*
    tags: {
        testid: __ENV.TEST_ID || 'coupon-issue',
        loadgen: __ENV.LOADGEN || 'pc-01',
    },
    stages: [
        {duration: '30s', target: 500},
        {duration: '1m', target: 2000},
        {duration: '30s', target: 0},
    ],
    */

    // burst 부하 테스트
    scenarios: {
        burst_test: {
            executor: 'per-vu-iterations',
            vus: 2000,
            iterations: 1,
            maxDuration: '1m',
            tags: {
                test_type: __ENV.TEST_ID || 'coupon-issue',
                stage: __ENV.LOADGEN || 'pc-01',
            },
        },
    },
};

const baseUrl = __ENV.BASE_URL || 'http://192.168.0.27:8080';
const resourceId = __ENV.RESOURCE_ID || 'christmas';
const domain = __ENV.DOMAIN || 'COUPON';
const couponDetailId = __ENV.COUPON_DETAIL_ID || 1;

export default function () {
    const vuIndex = __VU - 1;
    const token = getToken(vuIndex);
    const headers = {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
    };

    // 쿠폰 발급 시작
    couponIssueFlow(headers);

    // 1회만 요청하기 위해 sleep
    sleep(160);
}

// 1. 쿠폰 이벤트 페이지 입장
function couponIssueFlow(headers) {
    group('Coupon Issue Flow', () => {
        const eventUrl = `${baseUrl}/api/event/coupon/${resourceId}`;
        const eventRes = http.get(eventUrl, {
            headers: headers,
            redirects: 0,
        });

        requestCounter.add(1);

        check(eventRes, {
            'event request completed': (r) => r.status === 308 || r.status === 200 || r.status === 201,
        });

        // 308 리다이렉트 = 대기열 필요
        if (eventRes.status === 308) {
            queueRedirectRate.add(1);
            handleQueueFlow(headers);
        }
        // 바로 입장
        else if (eventRes.status === 200 || eventRes.status === 201) {
            directIssueRate.add(1);
            issueCoupon(headers);
        }
        // 기타 에러
        else {
            requestFailureRate.add(1);
            console.error(`[VU ${__VU}] Event request failed with status ${eventRes.status}`);
        }
    });
}

// 2. 대기열 진입 요청
function handleQueueFlow(headers) {
    group('Enter Queue', () => {
        const enterUrl = `${baseUrl}/api/queue/enter`;
        const payload = JSON.stringify({
            "domain": domain,
            "resourceId": resourceId
        });
        const enterRes = http.post(enterUrl, payload, {headers: headers});

        requestCounter.add(1);

        const enterPassed = check(enterRes, {
            'queue enter status is 200': (r) => r.status === 200,
        });

        // 대기열 진입 실패 시 중단
        if (!enterPassed) {
            requestFailureRate.add(1);
            console.error(`[VU ${__VU}] Queue enter failed with status ${enterRes.status}`);
            return;
        }
    });

    // 본인 순서, 입장여부를 받기 위한 폴링
    group('Queue Polling', () => {
        let queueStatus = null;
        let pollingAttempt = 0;

        while (queueStatus !== 2) {
            pollingAttempt++;

            const queueOrderUrl = `${baseUrl}/api/queue/order?domain=${domain}&resourceId=${resourceId}`;
            const queueOrderRes = http.get(queueOrderUrl, {headers: headers});

            requestCounter.add(1);

            const orderCheckPassed = check(queueOrderRes, {
                'queue order status is 200': (r) => r.status === 200,
            });

            if (orderCheckPassed) {
                try {
                    const body = JSON.parse(queueOrderRes.body);
                    queueStatus = body.data.status;

                } catch (e) {
                    requestFailureRate.add(1);
                    console.error(`[VU ${__VU}] Failed to parse queue order response: ${e}`);
                }
            } else {
                requestFailureRate.add(1);
                console.error(`[VU ${__VU}] Queue order request failed, status: ${queueOrderRes.status}, message: ${queueOrderRes.body.message}`);
            }

            // 폴링 간격
            sleep(0.4);
        }

    });


    // Running Queue 진입 완료
    // - heartbeat 1회 호출
    sendHeartbeat(headers);

    // 쿠폰 발급 요청
    issueCoupon(headers);
}

// heartbeat 요청
// running queue 지속
function sendHeartbeat(headers) {
    group('Queue Heartbeat', () => {
        const heartbeatUrl = `${baseUrl}/api/queue/heartbeat`;
        const payload = JSON.stringify({
            "domain": domain,
            "resourceId": resourceId
        });
        const heartbeatRes = http.patch(heartbeatUrl, payload, {headers: headers});

        requestCounter.add(1);

        const heartbeatPassed = check(heartbeatRes, {
            'heartbeat status is 200 or 204': (r) => r.status === 200 || r.status === 204,
        });

        if (!heartbeatPassed) {
            requestFailureRate.add(1);
            console.error(`[VU ${__VU}] Heartbeat failed with status ${heartbeatRes.status}`);
        }
    });
}

// 쿠폰 발급 처리
function issueCoupon(headers) {
    group('Issue Coupon', () => {
        const issueCouponUrl = `${baseUrl}/api/coupon/${couponDetailId}`;
        const payload = JSON.stringify({
            "resourceId": resourceId
        });
        const issueCouponRes = http.post(issueCouponUrl, payload, {headers: headers});

        requestCounter.add(1);

        const issuePassed = check(issueCouponRes, {
            'coupon issue status is 200 or 201': (r) => r.status === 200 || r.status === 201,
        });

        if (issuePassed) {
            couponIssueSuccessRate.add(1);
        } else {
            couponIssueSuccessRate.add(0);
            requestFailureRate.add(1);
            console.error(`[VU ${__VU}] 쿠폰 발급 실패 with status ${issueCouponRes.status}: ${issueCouponRes.body}`);
        }
    });
}
