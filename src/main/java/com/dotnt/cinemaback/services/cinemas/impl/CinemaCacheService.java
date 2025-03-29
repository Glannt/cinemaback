package com.dotnt.cinemaback.services.cinemas.impl;

import com.dotnt.cinemaback.dto.CinemaDTO;
import com.dotnt.cinemaback.mapper.CinemaMapper;
import com.dotnt.cinemaback.models.Cinema;
import com.dotnt.cinemaback.redis.IRedisService;
import com.dotnt.cinemaback.redission.RedisDistributedLocker;
import com.dotnt.cinemaback.repositories.CinemaRepository;
import com.dotnt.cinemaback.services.cinemas.ICinemaRedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.dotnt.cinemaback.redission.RedisDistributedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
@RequiredArgsConstructor
public class CinemaCacheService implements ICinemaRedisService {
    private final RedisDistributedService redisDistributedService;
     // Khai bao cache
    private final IRedisService redisService;

//    private final CinemaService cinemaService;

    private final CinemaRepository cinemaRepository;

    private final CinemaMapper cinemaMapper;

    private final ObjectMapper objectMapper;

    private final static Cache<UUID, CinemaDTO> cinemaLocalCache = CacheBuilder
        .newBuilder()
        .initialCapacity(10)
        .concurrencyLevel(12)
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .build();

//    public CinemaCache getCinemaCacheById(UUID cinemaId, Long version)
    @Override
    public CinemaDTO getCinemaCacheById(UUID cinemaId){
        //get data from local cache
        CinemaDTO cinemaCache = getCinemaFromLocalCache(cinemaId);
        if (cinemaCache != null) {
            return cinemaCache;
        }
        return getCinemaDistributedCache(cinemaId);
    }
    /**
     * get cinema from database
     */
    public CinemaDTO getCinemaDetailDatabase(UUID cinemaId) {
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock(genEventItemKeyLock(cinemaId));
        try {
            // 1 - Tao lock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
            if (!isLock) {
                return null; // return retry
            }
            // Get cache
            CinemaDTO cinemaCache = redisService.getObject(genEventItemKey(cinemaId), CinemaDTO.class);
            // 2. YES
            if (cinemaCache != null) {
                return cinemaCache;
            }
            Cinema cinema = cinemaRepository.findById(cinemaId).orElse(null);

             cinemaCache = cinemaMapper.toDto(cinema);
            // set data to distributed cache
            redisService.setObject(genEventItemKey(cinemaId), cinemaCache, 20, TimeUnit.MINUTES);
            return cinemaCache;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            locker.unlock();
        }
    }


    /**
     * get cinema from distributed cache
     */
    public CinemaDTO getCinemaDistributedCache(UUID cinemaId) {
        // 1 - get data
        CinemaDTO cinemaCache = redisService.getObject(genEventItemKey(cinemaId), CinemaDTO.class);
        if(cinemaCache == null){
            log.info("GET CINEMA FROM DISTRIBUTED LOCK");
            cinemaCache = getCinemaDetailDatabase(cinemaId);
        }
        // 2 - put data to local cache
        // lock()
        cinemaLocalCache.put(cinemaId, cinemaCache); //.. consistency cache
        // unLock()
        log.info("GET TICKET FROM DISTRIBUTED CACHE");
        return cinemaCache;
    }

    public CinemaDTO getCinemaFromLocalCache(UUID cinemaId) {
        return cinemaLocalCache.getIfPresent(cinemaId);
    }

    Cache<String, Page<CinemaDTO>> listCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(12)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    @Override
    public Page<CinemaDTO> getAllCinemaCache(int page, int limit) throws JsonProcessingException {
        PageRequest pageRequest = PageRequest.of(page, limit);
        String key = genListKey(pageRequest);
        Page<CinemaDTO> cinemasCache = getCinemasFromLocalCache(key);
        if(cinemasCache != null){
            return cinemasCache;
        }
        return getCinemasDistributionCache(pageRequest);
    }

    public Page<CinemaDTO> getCinemasFromLocalCache(String key) {

        return listCache.getIfPresent(key);
    }

    public Page<CinemaDTO> getCinemasDistributionCache(PageRequest pageRequest) throws JsonProcessingException {
        String key = genListKey(pageRequest);
        String json = redisService.getString(key);
        Page<CinemaDTO> cinemasCache = json != null ? objectMapper.readValue(json,
                new TypeReference<>() {
                }) : null;
        if (cinemasCache == null) {
            log.info("GET CINEMAS FROM DISTRIBUTED LOCK");
            cinemasCache = getCinemasDetailDatabase(pageRequest);
        }
        listCache.put(key, cinemasCache);
        String jsonString = objectMapper.writeValueAsString(cinemasCache);
        redisService.setString(key, jsonString, 20, TimeUnit.MINUTES);
        return cinemasCache;
    }

    public Page<CinemaDTO> getCinemasDetailDatabase(PageRequest pageRequest) {
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock(genListKeyLock(pageRequest));
        try {
            // 1 - Tao lock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
            if (!isLock) {
                return null; // return retry
            }
            // Get cache
            String json = redisService.getString(genListKey(pageRequest));
            Page<CinemaDTO> cinemasCache = json != null ? objectMapper.readValue(json,
                    new TypeReference<>() {
                    }) : null;
            // 2. YES
            if (cinemasCache != null) {
                return cinemasCache;
            }
            Pageable pageable = PageRequest.of(pageRequest.getPageNumber() - 1, pageRequest.getPageSize(), Sort.by("createdAt").descending());
            Page<Cinema> cinemas = cinemaRepository.findActiveCinemas(pageable);

            Page<CinemaDTO> cinemaDTOs = cinemas.map(cinemaMapper::toDto);
            // set data to distributed cache
            String jsonString = objectMapper.writeValueAsString(cinemaDTOs);
            redisService.setString(genListKey(pageRequest), jsonString, 20, TimeUnit.MINUTES);
            return cinemaDTOs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            locker.unlock();
        }
    }


    private String genListKey(PageRequest pageRequest){
        return "LIST_CINEMA:PAGE" + pageRequest.getPageNumber() + ":SIZE" + pageRequest.getPageSize();
    }

    private String genListKeyLock(PageRequest pageRequest) {
        return "LIST_CINEMA_LOCK:PAGE" + pageRequest.getPageNumber() + ":SIZE" + pageRequest.getPageSize();
    }

    private String genEventItemKey(UUID cinemaId) {
        return "PRO_CINEMA:ITEM:" + cinemaId;
    }

    private String genEventItemKeyLock(UUID cinemaId) {
        return "PRO_LOCK_KEY_ITEM:" + cinemaId;
    }
}
