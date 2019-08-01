package com.ck.miaosha.redis;

import redis.clients.jedis.Jedis;

public class RedisTool {
    private static final String LOCK_SUCCESS ="OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PK";
    private static final Long RELEASE_SUCCESS = 1L;

    /**
     *
     * @param jedis
     * @param lockKey 1.锁
     * @param requestId 2.通过value赋值给requestId，就知道这把锁就哪个请求加的了
     * @param expireTime 5.过期时间
     * 3.NX:key不存在时，set操作；key存在，不做任何操作
     * 4.给key加过期设置
     *                   由于单机redis，满足了可靠性的3个条件：互斥性（NX有就啥也不做，没有就加锁）、即使崩溃没解锁，锁也会因为到了过期时间而自动解锁、requestId校验
     * @return
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime){
        String result = jedis.set(lockKey,requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME,expireTime);
        if(LOCK_SUCCESS.equals(result)){
            return true;
        }
        return false;
    }










}
