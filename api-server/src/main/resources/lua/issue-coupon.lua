-- 쿠폰 발급 검증, 발행 처리

-- KEYS[1] = 쿠폰상세 키
-- KEYS[2] = 쿠폰재고 키
-- KEYS[3] = 멤버 키
-- ARGV[1] = 멤버 아이디
-- ARGV[2] = 현재시간

-- 반환값: -3(쿠폰 상세 미등록), -2(이벤트 일자가 아님), -1(중복 발급한 유저), 0(재고 없음), 그 외 발급 순번 반환

local couponDetailKey = KEYS[1]
local issuedQuantityKey = KEYS[2]
local issuedMemberKey = KEYS[3]
local memberId = ARGV[1]
local currentTime = tonumber(ARGV[2])

-- 쿠폰 상세 확인
local detail = redis.call('HGETALL', couponDetailKey)
if #detail == 0 then
    return -3
end

-- 쿠폰 상세 값 map 으로 변환
local detailMap = {}
for i = 1, #detail, 2 do
    detailMap[detail[i]] = detail[i + 1]
end

local publishedAt = tonumber(detailMap['publishedAt'])
local expiredAt = tonumber(detailMap['expiredAt'])
local totalQuantity = tonumber(detailMap['totalQuantity'])

-- 발행일 & 만기일 검증
if currentTime < publishedAt or currentTime > expiredAt then
    return -2
end

-- 유저 중복 발급 확인
local existsMember = redis.call('SISMEMBER', issuedMemberKey, memberId)
if existsMember == 1 then
    return -1
end

-- 재고 검증
local currentCount = redis.call('GET', issuedQuantityKey)
if currentCount == totalQuantity then
    -- 매진
    return 0
end

-- 발급 카운트 증가
local incrCount = redis.call('INCR', issuedQuantityKey)

-- 발급받은 멤버 추가
redis.call('SADD', issuedMemberKey, memberId)

-- 발급 카운트(순번) 반환
return tonumber(incrCount)
