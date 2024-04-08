local key = KEYS[1] -- unique key
local limitCount = tonumber (ARGV[1]) -- limit size
local limitTime = tonumber (ARGV[2]) --  limiting time
local current = tonumber(redis.call('get', key) or '0')

if current + 1 > limitCount then -- 분당호출수 초과시 0 return
    return 0
elseif current == 0 then
    redis.call('INCRBY', key, '1')
    redis.call('expire', key,limitTime)
    return current + 1
else
    redis.call('INCRBY', key, '1')
    return current + 1
end