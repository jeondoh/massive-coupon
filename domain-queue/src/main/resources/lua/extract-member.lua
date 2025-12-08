-- running queue에서 timeout된 멤버 및 해당 토큰 제거

-- KEYS[1] = Running Queue 키
-- ARGV[1] = 삭제 기준시간 (밀리초)
-- ARGV[2] = 토큰 키값

-- 반환값: 제거된 멤버 리스트

local runningQueueKey = KEYS[1]
local timeoutScore = tonumber(ARGV[1])
local tokenKeyPrefix = ARGV[2]

-- score가 timeoutScore 이하인 멤버들 조회
local members = redis.call('ZRANGEBYSCORE', runningQueueKey, '-inf', timeoutScore)

if #members == 0 then
    return {}
end

-- 조회된 멤버들을 running queue에서 제거, 토큰 삭제
for _, member in ipairs(members) do
    redis.call('ZREM', runningQueueKey, member)

    local tokenKey = tokenKeyPrefix .. ":" .. member
    redis.call('DEL', tokenKey)
end

return members
