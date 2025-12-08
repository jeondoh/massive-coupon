-- 멤버의 대기순번, 대기자 수 가져오기

-- KEYS = Waiting Queue 키
-- ARGV = Domain:resourceId:memberId (member 값)

-- 반환값: List {status, rank, total}
-- - status: 0(없음), 1(성공)
-- - rank: 대기 순번
-- - total: 전체 대기자 수

local waitingQueueKey = KEYS[1]
local memberValue = ARGV[1]

-- 대기 순번 가져오기
local rank = redis.call('ZRANK', waitingQueueKey, memberValue)

if not rank then
    return { 0, 0, 0 }
end

-- 전체 대기자 수 가져오기
local total = redis.call('ZCARD', waitingQueueKey)

return { 1, tonumber(rank), tonumber(total) }
