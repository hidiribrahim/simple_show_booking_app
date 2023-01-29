package com.hidir.show.service;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.util.HashMap;

public class CacheService {


        private CacheManager cacheManager;
        private Cache<Integer, HashMap> seatsCache;

        public CacheService() {
            cacheManager = CacheManagerBuilder
                    .newCacheManagerBuilder().build();
            cacheManager.init();

            seatsCache = cacheManager
                    .createCache("seatsCache", CacheConfigurationBuilder
                            .newCacheConfigurationBuilder(
                                    Integer.class, HashMap.class,
                                    ResourcePoolsBuilder.heap(10)));
        }

        public Cache<Integer, HashMap> getSeatsCacheFromCacheManager() {
            return cacheManager.getCache("seatsCache", Integer.class, HashMap.class);
        }

        // standard getters and setters

}
