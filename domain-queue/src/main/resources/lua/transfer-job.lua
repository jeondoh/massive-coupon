-- Waiting → Running 큐 이동

-- KEYS[1] = Waiting Queue 키
-- KEYS[2] = Running Queue 키
-- ARGV[1] = transferSize (한 번에 이동할 최대 인원 수)
-- ARGV[2] = threshold (Running Queue 최대 크기)
-- ARGV[3] = currentTime (score 값)

-- 반환값: 이동된 멤버 리스트

local waitingQueueKey = KEYS[1]
local runningQueueKey = KEYS[2]
local transferSizeValue = ARGV[1]
local thresholdName = ARGV[2]
local currentTime = ARGV[3]

-- Running Queue 현재 크기 조회
local runningCount = redis.call('ZCARD', runningQueueKey)
local threshold = tonumber(thresholdName)

-- threshold 초과시 이동 X
if runningCount > threshold then
    return {}
end

-- 이동 가능한 인원 계산
local moveCount = threshold - runningCount
local resultMoveCount = math.min(transferSizeValue, moveCount)

if resultMoveCount <= 0 then
    return {}
end

-- Waiting Queue에서 이동할 멤버들 조회
local members = redis.call('ZRANGE', waitingQueueKey, 0, resultMoveCount - 1)

if #members == 0 then
    return {}
end

-- Running Queue에 추가
for _, member in ipairs(members) do
    redis.call('ZADD', runningQueueKey, currentTime, member)
end

-- Waiting Queue에서 제거
redis.call('ZREMRANGEBYRANK', waitingQueueKey, 0, #members - 1)

return members
