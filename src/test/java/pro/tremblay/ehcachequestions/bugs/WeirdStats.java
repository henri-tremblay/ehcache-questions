package pro.tremblay.ehcachequestions.bugs;

import pro.tremblay.ehcachequestions.utils.Utils;

import java.net.URI;

import javax.cache.Cache;
import javax.cache.CacheManager;

public class WeirdStats {

    private static final URI clusterURI = URI.create("terracotta://localhost:9510");
    private static final String cacheName = "weird";

    public static void main(String[] args) throws Exception {

//        localTest();
        serverTest();
    }

    private static void localTest() {
        try(CacheManager cacheManager = Utils.getLocalCacheManager(cacheName)) {

            Cache<Long, String> cache = cacheManager.getCache(cacheName, Long.class, String.class);

            for(long key = 0; key < 10; key++) {
                cache.put(key, "xxxx"); // out
                cache.get(key); // cache hit
                cache.get(1231241235L); // cache miss
            }

            System.out.println("First part done");
            Utils.printMetrics(cacheName);
        }

        try(CacheManager cacheManager = Utils.getLocalCacheManager(cacheName)) {
            Cache<Long, String> cache = cacheManager.getCache(cacheName, Long.class, String.class);
            Utils.printMetrics(cacheName);
        }
    }

    private static void serverTest() {
        try(CacheManager cacheManager = Utils.getClusteredCacheManager(cacheName)) {

            Cache<Long, String> cache = cacheManager.getCache(cacheName, Long.class, String.class);

            for(long key = 0; key < 1000; key++) {
                cache.put(key, "xxxxxxxxxxxxxxxxxxxxxxxx"); // out
                cache.get(key); // cache hit
                cache.get(1231241235L); // cache miss
            }

            System.out.println("First part done");
            Utils.printMetrics(cacheName);
        }

        try(CacheManager cacheManager = Utils.getClusteredCacheManager(cacheName)) {
            Cache<Long, String> cache = cacheManager.getCache(cacheName, Long.class, String.class);
            Utils.printMetrics(cacheName);
        }
    }

}
