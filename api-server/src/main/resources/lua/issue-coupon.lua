-- 쿠폰 발급 검증, 발행 처리

-- KEYS[1] = 쿠폰재고 키
-- KEYS[2] = 멤버 키
-- ARGV[1] = 멤버 아이디
-- ARGV[2] = 전체 재고 수량

-- 반환값: -1(중복 발급한 유저), 0(재고 없음), 그 외 발급 순번 반환

local issuedQuantityKey = KEYS[1]
local issuedMemberKey = KEYS[2]
local memberId = ARGV[1]
local totalQuantity = tonumber(ARGV[2])

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
