-- 멤버의 대기순번, 대기자 수 가져오기

-- KEYS[1] = Waiting Queue 키
-- KEYS[2] = Running Queue 키
-- ARGV = Domain:resourceId:memberId (member 값)

-- 반환값: List {status, rank, total}
-- - status: 0(없음), 1(성공), 2(큐 이동됨)
-- - rank: 대기 순번
-- - total: 전체 대기자 수

local waitingQueueKey = KEYS[1]
local runningQueueKey = KEYS[2]
local memberValue = ARGV[1]

-- 대기 순번 가져오기
local rank = redis.call('ZRANK', waitingQueueKey, memberValue)

if not rank then
    -- 큐 이동 여부 확인
    local isRunning = redis.call('ZSCORE', runningQueueKey, memberValue)
    if isRunning then
        return { 2, 0, 0 }
    end

    -- 아예 없다면
    return { 0, 0, 0 }
end

-- 전체 대기자 수 가져오기
local total = redis.call('ZCARD', waitingQueueKey)

return { 1, tonumber(rank), tonumber(total) }
