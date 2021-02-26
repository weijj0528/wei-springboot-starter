local res = {1,0}
local rollBackIndex = 0
local itemCount = ARGV[1]
for i = 1 , itemCount , 1 do
  local key = ARGV[2 + 3 * (i - 1)]
  local usable = tonumber(ARGV[3 + 3 * (i - 1)])
  local expire = tonumber(ARGV[4 + 3 * (i - 1)])
  local nowUsable = redis.call('HGET', key, 'usable') or 0
  local usableAdd = usable - nowUsable;
  nowUsable = redis.call('HINCRBY', key, 'usable', usableAdd)
  local nowTotal = redis.call('HINCRBY', key, 'total', usableAdd)
  res[3 + (i - 1)] = usableAdd
  local nowUsed = redis.call('HGET', key, 'used') or 0
  if nowUsable < 0 or nowTotal < 0 or nowTotal < nowUsed then
    rollBackIndex = i
    res[1] = 0
    res[2] = rollBackIndex
    break
  end
end
if rollBackIndex > 0 then
  for i = 1 , rollBackIndex , 1 do
    usableAdd = res[3 + (i - 1)]
    redis.call('HINCRBY', key, 'usable', 0 - usableAdd)
    redis.call('HINCRBY', key, 'total', 0 - usableAdd)
  end
end
if rollBackIndex == 0 then
  for i = 1 , itemCount , 1 do
    local key = ARGV[2 + 3 * (i - 1)]
    local expire = tonumber(ARGV[4 + 3 * (i - 1)])
    if expire < 0 then
       redis.call('PERSIST', key)
    elseif expire > 0 then
       redis.call('EXPIRE', key, expire)
    end
  end
end
return res