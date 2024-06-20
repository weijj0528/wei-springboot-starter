package com.wei.starter.redis.service.imp;

import cn.hutool.crypto.SecureUtil;
import com.wei.starter.base.bean.CodeEnum;
import com.wei.starter.base.bean.Result;
import com.wei.starter.base.exception.ErrorMsgException;
import com.wei.starter.redis.service.IRedisIncrService;
import com.wei.starter.redis.util.WeiRedisUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisScriptingCommands;
import org.springframework.data.redis.connection.ReturnType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The type Redis incr service.
 *
 * @author William.Wei
 */
public class RedisIncrServiceImpl implements IRedisIncrService {

    private final RedisConnectionFactory redisConnectionFactory;

    private final String scriptHas1;
    private final String hSetScriptHas1;

    /**
     * Instantiates a new Redis incr service.
     *
     * @param redisConnectionFactory the redis connection factory
     */
    public RedisIncrServiceImpl(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
        String script = WeiRedisUtils.readScript("lua/s_set.lua");
        String hSetScript = WeiRedisUtils.readScript("lua/h_set.lua");
        Optional.ofNullable(script).orElseThrow(() -> new ErrorMsgException("RedisIncrServiceImpl script read fail!"));
        Optional.ofNullable(hSetScript).orElseThrow(() -> new ErrorMsgException("RedisIncrServiceImpl has set script read fail!"));
        final RedisConnection connection = redisConnectionFactory.getConnection();
        final RedisScriptingCommands scriptingCommands = connection.scriptingCommands();
        scriptingCommands.scriptLoad(script.getBytes());
        scriptingCommands.scriptLoad(hSetScript.getBytes());
        connection.close();
        scriptHas1 = SecureUtil.sha1(script);
        hSetScriptHas1 = SecureUtil.sha1(hSetScript);
    }

    /**
     * Incr result.
     * 批量Key增量操作，保证操作后数量>=0
     * 如出现<0情况本次操作失败已操作Key回滚
     * 正数为增加负数为扣减
     * <p>
     * ***注意：批量操作的Key必须在同一节点
     *
     * @return the result
     */
    @Override
    public Result<Map<String, Long>> incr(String[] hashKey, Long[] incr, Integer[] expire) {
        RedisConnection connection = redisConnectionFactory.getConnection();
        byte[][] keyArgs = new byte[hashKey.length + incr.length + expire.length][];
        for (int i = 0; i < hashKey.length; i++) {
            keyArgs[i] = hashKey[i].getBytes();
            keyArgs[hashKey.length + i] = String.valueOf(incr[i]).getBytes();
            keyArgs[hashKey.length + incr.length + i] = String.valueOf(expire[i]).getBytes();
        }
        List<Long> eval = connection.evalSha(scriptHas1, ReturnType.fromJavaType(List.class), hashKey.length, keyArgs);
        connection.close();
        if (eval == null || eval.isEmpty()) {
            return Result.failure(CodeEnum.ERROR_SERVER.getCode(), "RedisIncrServiceImpl incr fail!");
        }
        Long aLong = eval.get(0);
        if (aLong < 0) {
            // 操作失败
            int i = -1 - aLong.intValue();
            return Result.failure(CodeEnum.ERROR_SERVER.getCode(), hashKey[i] + " 数量不足");
        }
        Map<String, Long> map = new HashMap<>();
        for (int j = 0; j < hashKey.length; j++) {
            map.put(hashKey[j], eval.get(1 + j));
        }
        return Result.success(map);
    }

    @Override
    public Result<Map<String, Long>> incr(Map<String, Long> keyArgs) {
        String[] hashKey = new String[keyArgs.size()];
        Long[] incr = new Long[keyArgs.size()];
        Integer[] expire = new Integer[keyArgs.size()];
        int i = 0;
        for (Map.Entry<String, Long> entry : keyArgs.entrySet()) {
            hashKey[i] = entry.getKey();
            incr[i] = entry.getValue();
            expire[i] = 0;
            i++;
        }
        return incr(hashKey, incr, expire);
    }

    @Override
    public Result<Map<String, Long>> getIncr(String[] keys) {
        Map<String, Long> map = new HashMap<>(keys.length);
        RedisConnection connection = redisConnectionFactory.getConnection();
        for (String key : keys) {
            byte[] bytes = connection.get(key.getBytes());
            if (bytes == null) {
                map.put(key, 0L);
            } else {
                map.put(key, Long.parseLong(new String(bytes)));
            }
        }
        connection.close();
        return Result.success(map);
    }

    /**
     * Hash key incr
     * 批量HashKey增量操作，保证操作后数量>=0
     * 如出现<0情况本次操作失败已操作Key回滚
     * 正数为增加负数为扣减
     *
     * @param key     the key
     * @param keyArgs the params
     * @return the result
     */
    @Override
    public Result<Map<String, Long>> hasIncr(String key, Integer expire, Map<String, Long> keyArgs) {
        String[] hashKey = new String[keyArgs.size()];
        String[] incr = new String[keyArgs.size()];
        int i = 0;
        for (Map.Entry<String, Long> entry : keyArgs.entrySet()) {
            hashKey[i] = entry.getKey();
            incr[i] = String.valueOf(entry.getValue());
            i++;
        }
        List<Long> hincr = hasIncr(key, expire, hashKey, incr);
        Long aLong = hincr.get(0);
        if (aLong < 0) {
            // 操作失败
            i = -1 - aLong.intValue();
            return Result.failure(CodeEnum.ERROR_SERVER.getCode(), hashKey[i] + " 数量不足");
        }
        Map<String, Long> map = new HashMap<>();
        for (int j = 0; j < hashKey.length; j++) {
            map.put(hashKey[j], hincr.get(1 + j));
        }
        return Result.success(map);
    }

    private List<Long> hasIncr(String hash, Integer expire, String[] hashKey, String[] incr) {
        RedisConnection connection = redisConnectionFactory.getConnection();
        byte[][] keyArgs = new byte[3 + hashKey.length + incr.length][];
        // key
        keyArgs[0] = hash.getBytes();
        // hash key长度
        keyArgs[1] = String.valueOf(hashKey.length).getBytes();
        // 过期时间
        keyArgs[2] = String.valueOf(expire).getBytes();
        for (int i = 0; i < hashKey.length; i++) {
            keyArgs[3 + i] = hashKey[i].getBytes();
            keyArgs[3 + hashKey.length + i] = incr[i].getBytes();
        }
        List<Long> eval = connection.evalSha(hSetScriptHas1, ReturnType.fromJavaType(List.class), 1, keyArgs);
        connection.close();
        return eval;
    }

    @Override
    public Result<Map<String, Long>> hasIncr(String key, Map<String, Long> keyArgs) {
        return hasIncr(key, 0, keyArgs);
    }

    @Override
    public Result<Map<String, Long>> getHasIncr(String key, String[] hashKeys) {
        Map<String, Long> map = new HashMap<>(hashKeys.length);
        RedisConnection connection = redisConnectionFactory.getConnection();
        for (String hashKey : hashKeys) {
            byte[] bytes = connection.hashCommands().hGet(key.getBytes(), hashKey.getBytes());
            if (bytes == null) {
                map.put(hashKey, 0L);
            } else {
                map.put(hashKey, Long.parseLong(new String(bytes)));
            }
        }
        connection.close();
        return Result.success(map);
    }
}
