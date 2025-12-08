-- 대기열 진입

-- KEYS[1] = Waiting Queue 키
-- KEYS[2] = Running Queue 키
-- ARGV[1] = Domain:resourceId:memberId (member 값)
-- ARGV[2] = currentTime (score 값, 밀리초)
-- ARGV[3] = queue token 키
-- ARGV[4] = queue token TTL

-- 반환값: {status, rank, total}
-- status: -1(이미 참여중), 0(이미 대기중), 1(성공)
-- rank: 대기 순번
-- total: 전체 대기자 수

local waitingQueueKey = KEYS[1]
local runningQueueKey = KEYS[2]
local memberValue = ARGV[1]
local currentTime = ARGV[2]
local queueTokenKey = ARGV[3]
local queueTokenTTL = ARGV[4]
local rank = 0
local total = 0

-- 중복 진입 방지, Waiting Queue 확인
local isWaiting = redis.call('ZSCORE', waitingQueueKey, memberValue)
if isWaiting then
    -- 이미 대기중일경우 기존 대기열을 유지 여부 클라에게 알림
    return { 0, rank, total }
end

-- 중복 진입 방지, Running Queue 확인
local isRunning = redis.call('ZSCORE', runningQueueKey, memberValue)
if isRunning then
    -- 이미 참여중
    return { -1, rank, total }
end

-- Waiting Queue에 추가
redis.call('ZADD', waitingQueueKey, currentTime, memberValue)

-- token 저장
redis.call('SET', queueTokenKey, currentTime, "EX", queueTokenTTL)

-- 대기 순번 조회
rank = redis.call('ZRANK', waitingQueueKey, memberValue)

-- 전체 대기자 수
total = redis.call('ZCARD', waitingQueueKey)

return { 1, tonumber(rank), tonumber(total) }
