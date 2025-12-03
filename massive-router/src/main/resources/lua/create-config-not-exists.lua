-- Config 존재 여부 확인
-- - 없으면 새로 생성

-- KEYS[1] = Default Config 키
-- KEYS[2] = Config 키

-- 반환값: config 신규 생성여부 (-1: 에러, 0: 미생성, 1: 생성)

local defaultConfigKey = KEYS[1]
local domainConfigKey = KEYS[2]
local isCreatedConfig = 0

-- Config 존재 여부 확인
local configExists = redis.call('EXISTS', domainConfigKey)

-- Config 없으면 Default에서 복제
if configExists == 0 then
    local defaultConfig = redis.call('HGETALL', defaultConfigKey)
    if #defaultConfig == 0 then
        return -1
    end

    redis.call('HSET', domainConfigKey, unpack(defaultConfig))
    isCreatedConfig = 1
end

return tonumber(isCreatedConfig)
