-- Config 존재 여부 확인
-- - 없으면 새로 생성

-- KEYS[1] = Default Config 키
-- KEYS[2] = Config 키
-- KEYS[3] = Config id 값
-- ARGV[1] = resourceId
-- ARGV[2] = domainType

-- 반환값: config 신규 생성여부 (-1: 에러, 0: 미생성, 1: 생성)

local defaultConfigKey = KEYS[1]
local domainConfigKey = KEYS[2]
local domainConfigId = KEYS[3]
local resourceId = ARGV[1]
local domainType = ARGV[2]
local isCreatedConfig = 0

-- Config 존재 여부 확인
local configExists = redis.call('EXISTS', domainConfigKey)

-- Config 없으면 Default에서 복제
if configExists == 0 then
    local defaultConfig = redis.call('HGETALL', defaultConfigKey)
    if #defaultConfig == 0 then
        return -1
    end

    for i = 1, #defaultConfig, 2 do
        if defaultConfig[i] == 'resourceId' then
            defaultConfig[i + 1] = resourceId
        elseif defaultConfig[i] == 'domainType' then
            defaultConfig[i + 1] = domainType
        elseif defaultConfig[i] == 'id' then
            defaultConfig[i + 1] = domainConfigId
        end
    end

    redis.call('HSET', domainConfigKey, unpack(defaultConfig))
    redis.call('SADD', 'config', domainConfigId)
    isCreatedConfig = 1
end

return tonumber(isCreatedConfig)
