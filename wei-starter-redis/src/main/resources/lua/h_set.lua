local res = {0}
local breakIndex = 0
local argvLength = ARGV[1]
local expire = tonumber(ARGV[2])
for i = 1 , argvLength , 1 do
    res[1+i] = redis.call('HINCRBY', KEYS[1], ARGV[2 + i], ARGV[2 + argvLength + i])
    if res[1+i] < 0 then
        breakIndex = i
        break
    end
end
if breakIndex > 0 then
    res[1] = 0 - breakIndex
    for i = 1 , breakIndex , 1 do
        local addNum = 0 - tonumber(ARGV[2 + argvLength + i])
        redis.call('HINCRBY', KEYS[1], ARGV[2 + i], addNum)
    end
end
if res[1] == 0 then
    if expire < 0 then
        redis.call('PERSIST', KEYS[1])
    elseif expire > 0 then
        redis.call('EXPIRE', KEYS[1], expire)
    end
end
return res