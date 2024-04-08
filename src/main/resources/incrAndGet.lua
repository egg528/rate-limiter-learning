local key = KEYS[1] -- unique key
local limitCount = tonumber (ARGV[1]) -- limit size
local limitTime = tonumber (ARGV[2]) --  limiting time
local current = tonumber(redis.call('get', key) or '0')

if current + 1 > limitCount then -- 분당호출수 초과시 0 return
    return 0
elseif current == 0 then
    redis.call('INCR', key)
    redis.call('EXPIRE', key, limitTime)
    return current + 1
else
    redis.call('INCR', key)
    return current + 1
end