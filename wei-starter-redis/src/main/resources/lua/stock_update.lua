local res = {1,0}
local rollBackIndex = 0
local itemCount = ARGV[1]
for i = 1 , itemCount , 1 do
  local key = ARGV[2 + 3 * (i - 1)]
  local usable = tonumber(ARGV[3 + 3 * (i - 1)])
  local lock = tonumber(ARGV[4 + 3 * (i - 1)])
  local usableAdd = usable;
  local lockAdd = lock;
  local usedAdd = 0 - usableAdd;
  if lock > 0 then
    usableAdd = usableAdd - lockAdd;
  end
  if lock < 0 then
    usedAdd = 0 - lockAdd - usable;
  end
  local nowUsable = redis.call('HINCRBY', key, 'usable', usableAdd)
  local nowLock = redis.call('HINCRBY', key, 'lock', lockAdd)
  local nowUsed = redis.call('HINCRBY', key, 'used', usedAdd)
  res[3 + 3 * (i - 1)] = nowUsable
  res[4 + 3 * (i - 1)] = nowLock
  res[5 + 3 * (i - 1)] = nowUsed
  if nowUsable < 0 or nowLock < 0 or nowUsed < 0 then
    rollBackIndex = i
    res[1] = 0
    res[2] = rollBackIndex
    break
  end
end
if rollBackIndex > 0 then
  for i = 1 , rollBackIndex , 1 do
    local key = ARGV[2 + 3 * (i - 1)]
    local usable = tonumber(ARGV[3 + 3 * (i - 1)])
    local lock = tonumber(ARGV[4 + 3 * (i - 1)])
    local usableAdd = usable;
    local lockAdd = lock;
    local usedAdd = 0 - usableAdd;
    if lock > 0 then
      usableAdd = usableAdd - lockAdd;
    end
    if lock < 0 then
      usedAdd = 0 - lockAdd - usable;
    end
    redis.call('HINCRBY', key, 'usable', 0 - usableAdd)
    redis.call('HINCRBY', key, 'lock', 0 - lockAdd)
    redis.call('HINCRBY', key, 'used', 0 - usedAdd)
  end
end
return res