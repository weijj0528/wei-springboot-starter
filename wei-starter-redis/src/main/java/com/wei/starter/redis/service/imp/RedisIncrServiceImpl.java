package com.wei.starter.redis.service.imp;

import com.wei.starter.base.bean.Result;
import com.wei.starter.base.exception.ErrorEnum;
import com.wei.starter.base.exception.ErrorMsgException;
import com.wei.starter.redis.service.IRedisIncrService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.ReturnType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Redis incr service.
 */
public class RedisIncrServiceImpl implements IRedisIncrService {

    private RedisConnectionFactory redisConnectionFactory;

    private final String script;
    private final String hSetScript;
    private final String hStockScript;

    /**
     * Instantiates a new Redis incr service.
     *
     * @param redisConnectionFactory the redis connection factory
     */
    public RedisIncrServiceImpl(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.script = readScript("lua/s_set.lua");
        this.hSetScript = readScript("lua/h_set.lua");
        this.hStockScript = readScript("lua/h_stock.lua");
    }

    /**
     * 脚本读取
     *
     * @param scriptName 脚本名称
     * @return
     */
    private String readScript(String scriptName) {
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(scriptName);
            inputStreamReader = new InputStreamReader(classPathResource.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line.trim()).append("\n");
                line = bufferedReader.readLine();
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    public Result<Map<String, Long>> getIncr(String[] keys) {
        Map<String, Long> map = new HashMap<>(keys.length);
        RedisConnection connection = redisConnectionFactory.getConnection();
        for (int i = 0; i < keys.length; i++) {
            byte[] bytes = connection.get(keys[i].getBytes());
            if (bytes == null) {
                map.put(keys[i], 0L);
            } else {
                map.put(keys[i], Long.parseLong(new String(bytes)));
            }
        }
        connection.close();
        return Result.success(map);
    }

    /**
     * Incr result.
     * 批量Key增量操作，保证操作后数量>=0
     * 如出现<0情况本次操作失败已操作Key回滚
     * 正数为增加负数为扣减
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
        List<Long> eval = connection.eval(script.getBytes(), ReturnType.fromJavaType(List.class), hashKey.length, keyArgs);
        connection.close();
        Long aLong = eval.get(0);
        if (aLong < 0) {
            // 操作失败
            int i = -1 - aLong.intValue();
            return Result.failure(ErrorEnum.ERROR_SERVER.getCode(), hashKey[i] + " 数量不足");
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
    public Result<Map<String, Long>> getHincr(String key, String[] hashKeys) {
        Map<String, Long> map = new HashMap<>(hashKeys.length);
        RedisConnection connection = redisConnectionFactory.getConnection();
        for (int i = 0; i < hashKeys.length; i++) {
            byte[] bytes = connection.hashCommands().hGet(key.getBytes(), hashKeys[i].getBytes());
            if (bytes == null) {
                map.put(hashKeys[i], 0L);
            } else {
                map.put(hashKeys[i], Long.parseLong(new String(bytes)));
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
    public Result<Map<String, Long>> hincr(String key, Integer expire, Map<String, Long> keyArgs) {
        String[] hashKey = new String[keyArgs.size()];
        String[] incr = new String[keyArgs.size()];
        int i = 0;
        for (Map.Entry<String, Long> entry : keyArgs.entrySet()) {
            hashKey[i] = entry.getKey();
            incr[i] = String.valueOf(entry.getValue());
            i++;
        }
        List<Long> hincr = hincr(key, expire, hashKey, incr);
        Long aLong = hincr.get(0);
        if (aLong < 0) {
            // 操作失败
            i = -1 - aLong.intValue();
            return Result.failure(ErrorEnum.ERROR_SERVER.getCode(), hashKey[i] + " 数量不足");
        }
        Map<String, Long> map = new HashMap<>();
        for (int j = 0; j < hashKey.length; j++) {
            map.put(hashKey[j], hincr.get(1 + j));
        }
        return Result.success(map);
    }

    private List<Long> hincr(String hash, Integer expire, String[] hashKey, String[] incr) {
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
        List<Long> eval = connection.eval(hSetScript.getBytes(), ReturnType.fromJavaType(List.class), 1, keyArgs);
        connection.close();
        return eval;
    }

    @Override
    public Result<Map<String, Long>> hincr(String key, Map<String, Long> keyArgs) {
        return hincr(key, 0, keyArgs);
    }

    @Override
    public Result<Map<String, Long>> hStockSet(String key, Map<String, Long> keyArgs) {
        RedisConnection connection = redisConnectionFactory.getConnection();
        ArrayList<String> list = new ArrayList<>(keyArgs.keySet());
        int size = list.size();
        byte[][] keys = new byte[2 + 2 * size][];
        keys[0] = key.getBytes();
        keys[1] = String.valueOf(list.size()).getBytes();
        for (int i = 0; i < size; i++) {
            keys[2 + i] = list.get(i).getBytes();
            keys[2 + size + i] = keyArgs.get(list.get(i)).toString().getBytes();
        }
        List<Long> eval = connection.eval(hStockScript.getBytes(), ReturnType.fromJavaType(List.class), 1, keys);
        connection.close();
        assert eval != null;
        Long result = eval.get(0);
        if (result < 0) {
            throw new ErrorMsgException(ErrorEnum.ERROR_SERVER.getCode(), "总库存更新出错，新设置数量应该大于已使用数量");
        }
        for (int i = 0; i < size; i++) {
            keyArgs.put(list.get(i), eval.get(i + 1));
        }
        return Result.success(keyArgs);
    }
}
