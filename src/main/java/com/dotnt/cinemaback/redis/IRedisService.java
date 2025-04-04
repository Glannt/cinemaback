package com.dotnt.cinemaback.redis;

import java.util.concurrent.TimeUnit;

public interface IRedisService {

    void setString(String key, String value);

    void setString(String key, String value, long expireTime, TimeUnit timeUnit);

    String getString(String key);

    void setObject(String key, Object value);

    void setObject(String key, Object value, long expireTime, TimeUnit timeUnit);

    <T> T getObject(String key, Class<T> targetClass);

    void delete(String key);
}
