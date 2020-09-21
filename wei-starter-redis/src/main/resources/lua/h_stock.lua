local res = {0}
local rollBackIndex = 0
local itemCount = ARGV[1]
for i = 1 , itemCount , 1 do
  local id = ARGV[1 + i]
  local newTotal = tonumber(ARGV[1 + itemCount + i])
  local totalKey = string.format("%s-total", id)
  local usedKey = string.format("%s-used", id)
  local used = redis.call('HGET', KEYS[1], usedKey) or 0
  if newTotal >= tonumber(used) then
    local nowTotal = redis.call('HGET', KEYS[1], totalKey) or 0
    local addNum = newTotal - tonumber(nowTotal)
    res[1 + i] = addNum
    if addNum ~= 0 then
      redis.call('HINCRBY', KEYS[1], id, addNum)
      redis.call('HINCRBY', KEYS[1], totalKey, addNum)
    end
  end
  if newTotal < tonumber(used) then
    rollBackIndex = i - 1
    res[1] = -1 - rollBackIndex
    break
  end
end
if rollBackIndex > 0 then
  for i = 1 , rollBackIndex , 1 do
    local id = ARGV[1 + i]
    local totalKey = string.format("%s-total", id)
    local usedKey = string.format("%s-used", id)
    local rollBackNum = 0 - res[1 + i]
    if rollBackNum ~= 0 then
      redis.call('HINCRBY', KEYS[1], id, rollBackNum)
      redis.call('HINCRBY', KEYS[1], totalKey, rollBackNum)
    end
  end
end
return res