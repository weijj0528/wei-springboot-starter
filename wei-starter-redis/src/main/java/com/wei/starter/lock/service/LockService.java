package com.wei.starter.lock.service;

import com.wei.starter.base.exception.ErrorMsgException;
import com.wei.starter.lock.WeiLock;
import com.wei.starter.lock.annotation.Lock;
import com.wei.starter.lock.annotation.Locking;
import com.wei.starter.lock.impl.MultipleLock;
import com.wei.starter.lock.impl.RedisLock;
import com.wei.starter.lock.impl.RedissonLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.Redisson;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Lock service.
 */
public class LockService {

    private final SpelExpressionParser parser = new SpelExpressionParser();

    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private final ConcurrentHashMap<String, Expression> cache = new ConcurrentHashMap<>(16);

    /**
     * redisson
     */
    private Redisson redisson;

    /**
     * 连接工厂
     */
    private RedisConnectionFactory redisConnectionFactory;


    /**
     * Instantiates a new Lock service.
     *
     * @param redisson the redisson
     */
    public LockService(Redisson redisson) {
        this.redisson = redisson;
    }

    /**
     * Instantiates a new Lock service.
     *
     * @param redisConnectionFactory the redis connection factory
     */
    public LockService(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    /**
     * 获取Redis锁
     *
     * @param lockKey the lock key
     * @return redis lock
     */
    public WeiLock getRedisLock(String lockKey) {
        if (redisson != null) {
            return new RedissonLock(lockKey, redisson);
        }
        if (redisConnectionFactory != null) {
            new RedisLock(lockKey, redisConnectionFactory);
        }
        throw new ErrorMsgException("redisson and redisConnectionFactory is null!");
    }

    /**
     * Gets redis lock.
     *
     * @param lockKey     the lock key
     * @param waitTime    the wait time
     * @param expiredTime the expired time
     * @return the redis lock
     */
    public RedisLock getRedisLock(String lockKey, long waitTime, long expiredTime) {
        return new RedisLock(lockKey, waitTime, expiredTime, redisConnectionFactory);
    }

    /**
     * 获取锁
     *
     * @param joinPoint the join point
     * @return wei lock
     */
    public WeiLock getWeiLock(ProceedingJoinPoint joinPoint) {
        final ArrayList<WeiLock> lockList = new ArrayList<>();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Object target = joinPoint.getTarget();
        final Method method = methodSignature.getMethod();
        final Object[] args = joinPoint.getArgs();
        ExpressionRootObject rootObject = new ExpressionRootObject(method, args, target, target.getClass());
        EvaluationContext evaluationContext = new MethodBasedEvaluationContext(rootObject, method, args, parameterNameDiscoverer);
        final Lock lock = method.getAnnotation(Lock.class);
        final Locking locking = method.getAnnotation(Locking.class);
        Optional.ofNullable(lock).ifPresent(l -> parserLock(lockList, l, evaluationContext));
        Optional.ofNullable(locking).ifPresent(ing ->
                Arrays.asList(ing.value()).forEach(l -> parserLock(lockList, l, evaluationContext))
        );
        return new MultipleLock(lockList);
    }

    private void parserLock(List<WeiLock> lockList, Lock lock, EvaluationContext evaluationContext) {
        String lockKey = lock.lockName();
        String key = lock.key();
        if (StringUtils.hasText(key)) {
            final Expression expression = getExpression(key);
            // TODO: 2021/2/20 解析出多个 Key
            key = expression.getValue(evaluationContext, String.class);
            lockKey = String.format("%s::%s", lockKey, key);
            lockList.add(getRedisLock(lockKey, lock.waitTime(), lock.expiredTime()));
        } else {
            lockList.add(getRedisLock(lockKey, lock.waitTime(), lock.expiredTime()));
        }
    }

    private Expression getExpression(String key) {
        Expression expression = cache.get(key);
        if (expression == null) {
            expression = parser.parseExpression(key);
            cache.put(key, expression);
        }
        return expression;
    }


    /**
     * The type Expression root object.
     */
    public static class ExpressionRootObject {

        private final Method method;

        private final Object[] args;

        private final Object target;

        private final Class<?> targetClass;

        /**
         * Instantiates a new Expression root object.
         *
         * @param method      the method
         * @param args        the args
         * @param target      the target
         * @param targetClass the target class
         */
        public ExpressionRootObject(Method method, Object[] args, Object target, Class<?> targetClass) {
            this.method = method;
            this.args = args;
            this.target = target;
            this.targetClass = targetClass;
        }

        /**
         * Gets method.
         *
         * @return the method
         */
        public Method getMethod() {
            return method;
        }

        /**
         * Get args object [ ].
         *
         * @return the object [ ]
         */
        public Object[] getArgs() {
            return args;
        }

        /**
         * Gets target.
         *
         * @return the target
         */
        public Object getTarget() {
            return target;
        }

        /**
         * Gets target class.
         *
         * @return the target class
         */
        public Class<?> getTargetClass() {
            return targetClass;
        }
    }
}
