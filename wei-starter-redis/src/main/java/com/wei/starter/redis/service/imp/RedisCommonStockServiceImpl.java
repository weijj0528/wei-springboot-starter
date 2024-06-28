package com.wei.starter.redis.service.imp;

import cn.hutool.crypto.SecureUtil;
import com.wei.starter.base.bean.CodeEnum;
import com.wei.starter.base.bean.Result;
import com.wei.starter.base.exception.ErrorMsgException;
import com.wei.starter.base.valid.*;
import com.wei.starter.redis.model.StockDTO;
import com.wei.starter.redis.model.StockInitDTO;
import com.wei.starter.redis.model.StockModifyDTO;
import com.wei.starter.redis.model.StockUpdateDTO;
import com.wei.starter.redis.service.IRedisCommonStockService;
import com.wei.starter.redis.util.WeiRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;
import java.util.*;

/**
 * The type Redis common stock service.
 * 通用库存操作
 *
 * @author William.Wei
 */
@Slf4j
@Validated
public class RedisCommonStockServiceImpl implements IRedisCommonStockService {

    private final RedisConnectionFactory redisConnectionFactory;

    private final String initScriptHas1;
    private final String updateScriptHas1;

    /**
     * Instantiates a new Redis incr service.
     *
     * @param redisConnectionFactory the redis connection factory
     */
    public RedisCommonStockServiceImpl(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
        String initScript = WeiRedisUtils.readScript("lua/stock_init.lua");
        String updateScript = WeiRedisUtils.readScript("lua/stock_update.lua");
        Optional.ofNullable(initScript).orElseThrow(() -> new ErrorMsgException("IRedisCommonStockServiceImpl init script read fail!"));
        Optional.ofNullable(updateScript).orElseThrow(() -> new ErrorMsgException("IRedisCommonStockServiceImpl update script read fail!"));
        final RedisConnection connection = redisConnectionFactory.getConnection();
        final RedisScriptingCommands scriptingCommands = connection.scriptingCommands();
        scriptingCommands.scriptLoad(initScript.getBytes());
        scriptingCommands.scriptLoad(updateScript.getBytes());
        connection.close();
        initScriptHas1 = SecureUtil.sha1(initScript);
        updateScriptHas1 = SecureUtil.sha1(updateScript);
    }

    @Override
    public Result<StockModifyDTO> stockSet(@Validated(Setting.class) StockInitDTO initDTO) {
        final Result<List<StockModifyDTO>> result = stockSet(Collections.singleton(initDTO));
        if (result.successfully()) {
            return Result.success(result.getData().get(0));
        }
        return Result.failure(result.getCode(), result.getMsg());
    }

    @Override
    public Result<List<StockModifyDTO>> stockSet(@Validated(Setting.class) Collection<StockInitDTO> initDTOList) {
        for (StockInitDTO stockInitDTO : initDTOList) {
            if (stockInitDTO.getExpire() == null || stockInitDTO.getExpire() <= 0) {
                stockInitDTO.setExpire(-1);
            }
        }
        RedisConnection connection = redisConnectionFactory.getConnection();
        int size = initDTOList.size();
        byte[][] keyArgs = new byte[1 + 3 * size][];
        keyArgs[0] = String.valueOf(size).getBytes();
        List<StockInitDTO> list = new ArrayList<>(initDTOList);
        for (int i = 0; i < size; i++) {
            final StockInitDTO dto = list.get(i);
            keyArgs[1 + 3 * i] = dto.getKey().getBytes();
            keyArgs[2 + 3 * i] = dto.getUsable().toString().getBytes();
            keyArgs[3 + 3 * i] = dto.getExpire().toString().getBytes();
        }
        List<Long> eval = connection.evalSha(initScriptHas1, ReturnType.fromJavaType(List.class), 0, keyArgs);
        connection.close();
        assert eval != null;
        Long result = eval.get(0);
        if (result <= 0) {
            final long index = eval.get(1) - 1;
            String key = new String(keyArgs[(int) index + 1]);
            log.warn("Stock set fail:{}", key);
            throw new ErrorMsgException(CodeEnum.SYSTEM_ERROR.getCode(), "库存更新出错");
        }
        final List<StockModifyDTO> data = new ArrayList<>(size);
        int s = (eval.size() - 2);
        for (int i = 0; i < s; i++) {
            final StockModifyDTO dto = new StockModifyDTO();
            dto.setKey(new String(keyArgs[1 + 2 * i]));
            dto.setUsable(eval.get(2 + i).intValue());
            data.add(dto);
        }
        return Result.success(data);
    }

    @Override
    public Result<StockDTO> getStock(String key) {
        final RedisConnection connection = redisConnectionFactory.getConnection();
        final Map<byte[], byte[]> map = connection.hashCommands().hGetAll(key.getBytes());
        final StockDTO stockDTO;
        if (map != null) {
            stockDTO = getStockDTO(key, map);
        } else {
            stockDTO = new StockDTO();
            stockDTO.setKey(key);
            stockDTO.setTotal(BigInteger.ZERO.intValue());
            stockDTO.setUsed(BigInteger.ZERO.intValue());
            stockDTO.setUsable(BigInteger.ZERO.intValue());
            stockDTO.setLock(BigInteger.ZERO.intValue());
        }
        connection.close();
        return Result.success(stockDTO);
    }

    private StockDTO getStockDTO(String key, Map<byte[], byte[]> map) {
        final StockDTO stockDTO = new StockDTO();
        stockDTO.setKey(key);
        for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
            final String hashKey = new String(entry.getKey());
            final Integer value = Integer.valueOf(new String(entry.getValue()));
            switch (hashKey) {
                case USABLE:
                    stockDTO.setUsable(value);
                    break;
                case LOCK:
                    stockDTO.setLock(value);
                    break;
                case USED:
                    stockDTO.setUsed(value);
                    break;
                case TOTAL:
                    stockDTO.setTotal(value);
                    break;
                default:
            }
        }
        return stockDTO;
    }

    @Override
    public Result<List<StockDTO>> getStock(Collection<String> keys) {
        final RedisConnection connection = redisConnectionFactory.getConnection();
        List<StockDTO> list = new ArrayList<>(keys.size());
        final RedisHashCommands hashCommands = connection.hashCommands();
        for (String key : keys) {
            final Map<byte[], byte[]> map = hashCommands.hGetAll(key.getBytes());
            if (map != null) {
                final StockDTO stockDTO = getStockDTO(key, map);
                list.add(stockDTO);
            }
        }
        connection.close();
        return Result.success(list);
    }

    @Override
    public Result<StockDTO> usableAdd(@Validated(Add.class) StockUpdateDTO updateDTO) {
        final Result<List<StockDTO>> result = usableAdd(Collections.singleton(updateDTO));
        if (result.successfully()) {
            return Result.success(result.getData().get(0));
        }
        return Result.failure(result.getCode(), result.getMsg());
    }

    @Override
    public Result<List<StockDTO>> usableAdd(@Validated(Add.class) Collection<StockUpdateDTO> updateDTOList) {
        updateDTOList.forEach(updateDTO -> {
            updateDTO.setFlag(false);
            updateDTO.setLock(0);
        });
        return batchUpdate(updateDTOList);
    }

    @Override
    public Result<StockDTO> usableSub(@Validated(Sub.class) StockUpdateDTO updateDTO) {
        final Result<List<StockDTO>> result = usableSub(Collections.singleton(updateDTO));
        if (result.successfully()) {
            return Result.success(result.getData().get(0));
        }
        return Result.failure(result.getCode(), result.getMsg());
    }

    @Override
    public Result<List<StockDTO>> usableSub(@Validated(Sub.class) Collection<StockUpdateDTO> updateDTOList) {
        updateDTOList.forEach(updateDTO -> {
            updateDTO.setFlag(false);
            updateDTO.setLock(0);
        });
        return batchUpdate(updateDTOList);
    }

    @Override
    public Result<StockDTO> lock(@Validated(Lock.class) StockUpdateDTO updateDTO) {
        final Result<List<StockDTO>> result = lock(Collections.singleton(updateDTO));
        if (result.successfully()) {
            return Result.success(result.getData().get(0));
        }
        return Result.failure(result.getCode(), result.getMsg());
    }

    @Override
    public Result<List<StockDTO>> lock(@Validated(Lock.class) Collection<StockUpdateDTO> updateDTOList) {
        updateDTOList.forEach(updateDTO -> {
            updateDTO.setFlag(false);
            updateDTO.setUsable(0);
        });
        return batchUpdate(updateDTOList);
    }

    @Override
    public Result<StockDTO> unLock(@Validated(Unlock.class) StockUpdateDTO updateDTO) {
        final Result<List<StockDTO>> result = unLock(Collections.singleton(updateDTO));
        if (result.successfully()) {
            return Result.success(result.getData().get(0));
        }
        return Result.failure(result.getCode(), result.getMsg());
    }

    @Override
    public Result<List<StockDTO>> unLock(@Validated(Unlock.class) Collection<StockUpdateDTO> updateDTOList) {
        updateDTOList.forEach(updateDTO -> updateDTO.setFlag(false));
        return batchUpdate(updateDTOList);
    }

    @Override
    public Result<List<StockDTO>> batchUpdate(@Validated(Update.class) Collection<StockUpdateDTO> updateDTOList) {
        RedisConnection connection = redisConnectionFactory.getConnection();
        int size = updateDTOList.size();
        byte[][] keyArgs = new byte[1 + 3 * size][];
        keyArgs[0] = String.valueOf(size).getBytes();
        List<StockUpdateDTO> list = new ArrayList<>(updateDTOList);
        for (int i = 0; i < size; i++) {
            final StockUpdateDTO dto = list.get(i);
            keyArgs[1 + 3 * i] = dto.getKey().getBytes();
            keyArgs[2 + 3 * i] = dto.getUsable().toString().getBytes();
            keyArgs[3 + 3 * i] = dto.getLock().toString().getBytes();
        }
        List<Long> eval = connection.evalSha(updateScriptHas1, ReturnType.fromJavaType(List.class), 0, keyArgs);
        connection.close();
        assert eval != null;
        Long result = eval.get(0);
        if (result <= 0) {
            final long index = eval.get(1) - 1;
            String key = new String(keyArgs[(int) index + 1]);
            final Long nowUsable = eval.get(2);
            final Long nowLock = eval.get(3);
            final Long nowUsed = eval.get(4);
            log.warn("Stock update fail:{} Usable[{}],Lock[{}],Used[{}]", key, nowUsable, nowLock, nowUsed);
            throw new ErrorMsgException(CodeEnum.SYSTEM_ERROR.getCode(), "库存更新出错");
        }
        final List<StockDTO> data = new ArrayList<>(size);
        int s = (eval.size() - 2) / 3;
        for (int i = 0; i < s; i++) {
            final StockDTO dto = new StockDTO();
            dto.setKey(new String(keyArgs[1 + 3 * i]));
            dto.setUsable(eval.get(2 + 3 * i).intValue());
            dto.setLock(eval.get(3 + 3 * i).intValue());
            dto.setUsed(eval.get(4 + 3 * i).intValue());
            data.add(dto);
        }
        return Result.success(data);
    }
}
