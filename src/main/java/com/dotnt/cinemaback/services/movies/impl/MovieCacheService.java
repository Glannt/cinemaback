package com.dotnt.cinemaback.services.movies.impl;

import com.dotnt.cinemaback.dto.response.MovieResponseDTO;
import com.dotnt.cinemaback.mapper.MovieMapper;
import com.dotnt.cinemaback.models.Movie;
import com.dotnt.cinemaback.models.cache.MovieCache;
import com.dotnt.cinemaback.redis.IRedisService;
import com.dotnt.cinemaback.redission.RedisDistributedLocker;
import com.dotnt.cinemaback.redission.RedisDistributedService;

import com.dotnt.cinemaback.repositories.MovieRepository;
import com.dotnt.cinemaback.services.movies.IMovieCacheService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieCacheService implements IMovieCacheService {

    private final RedisDistributedService redisDistributedService;

    private final IRedisService redisService;

    private final MovieService movieService;

    private final MovieMapper movieMapper;

    private final MovieRepository movieRepository;

    private final ObjectMapper objectMap;

    private final static Cache<String, MovieResponseDTO> movieLocalCache = CacheBuilder
        .newBuilder()
        .initialCapacity(10)
        .concurrencyLevel(12)
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .removalListener(notification -> log.info("Remove cache: {}", notification.getKey()))
        .build();

    @Override
    public MovieResponseDTO getMovieCacheById(UUID movieId) {
        //get data from local cache
        MovieResponseDTO movieCache = getMovieFromLocalCache(movieId);
        if (movieCache != null) {
//            if (version == null){
//                log.info("01: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal: {}", version, movieCache.getVersion());
//                return movieCache;
//            }
//
//            if (version.equals(movieCache.getVersion())){
//                log.info("02: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal: {}", version, movieCache.getVersion());
//                return movieCache;
//            }
//
//            // version < ticketDetailCache.getVersion()
//            if (version < movieCache.getVersion()){
//                log.info("03: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal: {}", version, movieCache.getVersion());
//                return movieCache;
//            }
//
//            if (version > movieCache.getVersion()){
//                return getMovieDistributedCache(movieId);
//            }
            return getMovieDistributedCache(movieId);
        }
        return null;
    }

    public MovieResponseDTO getMovieFromLocalCache(UUID movieId) {
        return movieLocalCache.getIfPresent(movieId.toString());
    }

    public MovieResponseDTO getMovieDistributedCache(UUID movieId) {
        MovieResponseDTO movieCache = redisService.getObject(genEventItemKey(movieId), MovieResponseDTO.class);
        if (movieCache == null) {
            log.info("GET MOVIE FROM DISTRIBUTED LOCK");
            movieCache = getMovieDetailDatabase(movieId);
        }

        movieLocalCache.put(movieId.toString(), movieCache);
        return movieCache;
    }

    public MovieResponseDTO getMovieDetailDatabase(UUID movieId){
       RedisDistributedLocker locker = redisDistributedService.getDistributedLock(genEventItemKeyLock(movieId));
        try{
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            if(!isLock){
                return null;
            }
            MovieResponseDTO movieCache = redisService.getObject(genEventItemKey(movieId), MovieResponseDTO.class);
            if (movieCache != null) {
                return movieCache;
            }
            String movieStringId = movieId.toString();
            MovieResponseDTO movieDTO = movieService.getMovie(movieStringId);
            Movie movie = movieMapper.toEntity(movieDTO);
//            movieCache = new MovieCache().withClone(movie).withVersion(System.currentTimeMillis());
            movieCache = movieMapper.toDto(movie);
            redisService.setObject(genEventItemKey(movieId), movieCache, 20, TimeUnit.MINUTES);
            return movieCache;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            locker.unlock();
        }
    }

    private String genEventItemKey(UUID movieId) {
        return "movie:" + movieId;
    }

    private String genEventItemKeyLock(UUID movieId) {
        return "movie:lock:" + movieId;
    }

    private final static Cache<String, Page<MovieResponseDTO>> movieListLocalCache = CacheBuilder
            .newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(12)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .removalListener(notification -> log.info("Remove cache: {}", notification.getKey()))
            .build();

    @Override
    public Page<MovieResponseDTO> getMovies(int page, int limit) throws JsonProcessingException {
        PageRequest pageRequest = PageRequest.of(page, limit);
        String key = genListKey(pageRequest);
        Page<MovieResponseDTO> movieCacheList = getMoviesShowingFromLocalCache(key);

        if (!movieCacheList.isEmpty()) {

            return movieCacheList;
        }
        return getMoviesFromDistributedCache(pageRequest);

    }

    public Page<MovieResponseDTO> getMoviesFromDistributedCache(PageRequest pageRequest) throws JsonProcessingException {
        String key = genListKey(pageRequest);
        String json = redisService.getString(key);
        Page<MovieResponseDTO> movieCacheList = json != null
                ? objectMap.readValue(json, new TypeReference<Page<MovieResponseDTO>>() {})
                : null;
        if(movieCacheList == null){
            movieCacheList = getMoviesShowingDatabase(pageRequest);
        }

        movieListLocalCache.put(key, movieCacheList);
        return movieCacheList;
    }

    public Page<MovieResponseDTO> getMoviesShowingFromLocalCache(String key) {
        return movieListLocalCache.getIfPresent(key);
    }

    private Page<MovieResponseDTO> getMoviesShowingDatabase(PageRequest pageRequest) {
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock(genListLockKey(pageRequest));
        try {
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLock) {
                return null;
            }
            String key = genListKey(pageRequest);
            String json = redisService.getString(key);
            Page<MovieResponseDTO> movieCacheList = json != null
                    ? objectMap.readValue(json, new TypeReference<>() {
            })
                    : null;

            if(movieCacheList != null){
                return movieCacheList;
            }

            Page<Movie> movieList = movieRepository.findAll(pageRequest);

            return movieList.map(movieMapper::toDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            locker.unlock();
        }

    }

    private String genListKey(PageRequest pageRequest) {
        return "movies:showing:" + pageRequest.getPageNumber() + ":" + pageRequest.getPageSize();
    }

    private String genListLockKey(PageRequest pageRequest) {
        return "movies:showing:lock:" + pageRequest.getPageNumber() + ":" + pageRequest.getPageSize();
    }


}
