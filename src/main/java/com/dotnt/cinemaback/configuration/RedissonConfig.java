package com.dotnt.cinemaback.configuration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value(value = "${spring.data.redis.host}")
    private String redisAddress;

    @Value(value = "${spring.data.redis.port}")
    private String redisPort;

    @Bean
    public RedissonClient redissonClient() {
        org.redisson.config.Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6319").setConnectionPoolSize(50).setDatabase(0);
        config.useSingleServer().setAddress("redis://" + redisAddress + ":" + redisPort).setConnectionPoolSize(50).setDatabase(0);
        return Redisson.create(config);
    }
}
