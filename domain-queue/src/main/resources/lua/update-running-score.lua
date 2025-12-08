-- running 큐의 member score 값 업데이트

-- KEYS[1] = Running Queue 키
-- ARGV[1] = Domain:resourceId:memberId (member 값)
-- ARGV[2] = 업데이트할 score 값

-- 반환값: 성공여부 0(실패), 1(성공)

local runningQueueKey = KEYS[1]
local memberValue = ARGV[1]
local newScore = tonumber(ARGV[2])

-- Running Queue에서 해당 member의 score 업데이트
local isRunning = redis.call('ZSCORE', runningQueueKey, memberValue)

if isRunning then
    -- member가 존재하면 score 업데이트
    redis.call('ZADD', runningQueueKey, newScore, memberValue)
    return 1
else
    -- 없으면 실패
    return 0
end
