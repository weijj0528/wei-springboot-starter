local res = {0}
local breakIndex = 0
local argvLength = table.getn(ARGV) / 2
for i = 1 , argvLength , 1 do
    res[1+i] = redis.call('INCRBY', KEYS[i], ARGV[i])
    if res[1+i] < 0 then
        breakIndex = i
        break
    end
end
if breakIndex > 0 then
    res[1] = 0 - breakIndex
    for i = 1 , breakIndex , 1 do
        redis.call('DECRBY', KEYS[i], ARGV[i])
    end
end
if res[1] == 0 then
    for i = 1 , argvLength , 1 do
        local expire = tonumber(ARGV[argvLength + i])
        if expire < 0 then
            redis.call('PERSIST', KEYS[i])
        elseif expire > 0 then
            redis.call('EXPIRE', KEYS[i], expire)
        end
    end
end
return res