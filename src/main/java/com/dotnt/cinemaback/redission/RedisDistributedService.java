package com.dotnt.cinemaback.redission;

public interface RedisDistributedService {
    RedisDistributedLocker getDistributedLock(String lockKey);
}
