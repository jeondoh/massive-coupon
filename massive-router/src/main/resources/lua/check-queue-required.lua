-- 대기열 필요 여부 체크
-- - 트래픽 카운트 증가
-- - Waiting, Running Queue 크기 기반 체크
-- - RPM 기반 체크

-- KEYS[1] = traffic 키
-- KEYS[2] = Waiting Queue 키
-- KEYS[3] = Running Queue 키
-- KEYS[4] = Config 키
-- KEYS[5~10] = 최근 6개 Traffic 버킷 키
-- ARGV[1] = threshold 필드명
-- ARGV[2] = trafficRpm 필드명
-- ARGV[3] = traffic TTL(초)

-- 반환값: 대기열 필요 여부 (0: 불필요, 1: 필요)

local trafficKey = KEYS[1]
local waitingQueueKey = KEYS[2]
local runningQueueKey = KEYS[3]
local domainConfigKey = KEYS[4]
local thresholdName = ARGV[1]
local trafficRpmName = ARGV[2]
local ttl = ARGV[3]

-- 트래픽 카운트 증가
local trafficCount = redis.call('INCR', trafficKey)
if trafficCount == 1 then
    redis.call('EXPIRE', trafficKey, ttl)
end

-- Waiting Queue 크기 체크
local waitingCount = redis.call('ZCARD', waitingQueueKey)
if tonumber(waitingCount) > 0 then
    return 1
end

-- Running Queue 크기 체크
local runningCount = redis.call('ZCARD', runningQueueKey)
local threshold = redis.call('HGET', domainConfigKey, thresholdName)

if not threshold then
    return 0
end

local requiredByRunning = 0
if tonumber(runningCount) >= tonumber(threshold) then
    requiredByRunning = 1
end

-- Traffic 합계 계산
local trafficSum = trafficCount
for i = 5, 10 do
    local val = redis.call('GET', KEYS[i])
    if val then
        trafficSum = trafficSum + tonumber(val)
    end
end

-- Traffic RPM 체크
local trafficRpm = redis.call('HGET', domainConfigKey, trafficRpmName)

if not trafficRpm then
    return 0
end

local requiredByTraffic = 0
if trafficSum >= tonumber(trafficRpm) then
    requiredByTraffic = 1
end

-- 대기열 필요 여부 반환
local required = 0
if requiredByRunning == 1 or requiredByTraffic == 1 then
    required = 1
end

return tonumber(required)
