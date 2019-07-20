package com.juix.seckill.utils;

import com.juix.seckill.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-20 20:37
 **/
@Service
public class RedisPoolFactory {

    @Autowired
    RedisConfig redisConfig;

    @Bean
    public JedisPool JedisPoolFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();

        poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait());

        JedisPool pool = new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(),
                redisConfig.getTimeout() * 1000, redisConfig.getPassword() ,0);

        return pool;
    }
}
