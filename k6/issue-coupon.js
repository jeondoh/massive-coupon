import http from 'k6/http';
import {check, sleep} from 'k6';
import {Counter} from 'k6/metrics';
import {getToken} from "./auth.js";

const issueCouponCount = new Counter('coupon_issue_count');
const BASE_URL = __ENV.BASE_URL;
const COUPONDETAILID = 1;

export const options = {
    stages: [
        {duration: '2m', target: 1000},
        {duration: '1m', target: 0},
    ],
};

export default function () {
    const vuIndex = __VU - 1;
    const token = getToken(vuIndex);
    const headers = {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
    };
    const payload = JSON.stringify({
        resourceId: 'christmas'
    });
    const url = `${BASE_URL}/api/coupon/${COUPONDETAILID}`;
    const response = http.post(url, payload, {
        headers: headers,
        timeout: '10s',
    });

    // 응답 확인
    check(response, {
        'status is 200 (발급 성공)': (r) => r.status === 200,
        'status is 400 (중복 발급/마감)': (r) => r.status === 400,
        'status is 401 (인증 실패)': (r) => r.status === 401,
        'response has data or code field': (r) => {
            try {
                const body = JSON.parse(r.body);
                return 'data' in body || 'code' in body;
            } catch (e) {
                return false;
            }
        },
    });

    if (response.status === 200) {
        issueCouponCount.add(1);
    }

    sleep(180);
}
