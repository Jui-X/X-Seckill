package com.juix.seckill.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-20 20:38
 **/
@Data
@Component
@ConfigurationProperties("redis")
public class RedisConfig {
    private String host;
    private int port;
    private int timeout;//秒
    private String password;
    private int poolMaxTotal;
    private int poolMaxIdle;
    private int poolMaxWait;//秒
}
