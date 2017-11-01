package pro.tremblay.ehcachequestions.stackoverflow.q41350991;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import javax.cache.CacheException;

public class JsonObjectCacheManager {
    private static final Logger logger = LoggerFactory.getLogger(JsonObjectCacheManager.class);
    private final Cache<String, JsonObjectWrapper> objectCache;

    //setting up cache
    public JsonObjectCacheManager() {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
            .withCache("jsonCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, JsonObjectWrapper.class,
                    ResourcePoolsBuilder.newResourcePoolsBuilder()
                        .heap(100, EntryUnit.ENTRIES)
                        .offheap(10, MemoryUnit.MB))
                    .withExpiry(Expirations.timeToLiveExpiration(Duration.of(5, TimeUnit.MINUTES)))
                    .withValueSerializingCopier()
                    .build())
            .build(true);

        objectCache = cacheManager.getCache("jsonCache", String.class, JsonObjectWrapper.class);
    }

    public void putInCache(String key, Object value) {
        try {
            JsonObjectWrapper objectWrapper = new JsonObjectWrapper(value);
            objectCache.put(key, objectWrapper);
        } catch (CacheException e) {
            logger.error(String.format("Problem occurred while putting data into cache: %s", e.getMessage()));
        }
    }

    public Object retrieveFromCache(String key) {
        try {
            JsonObjectWrapper objectWrapper = objectCache.get(key);
            if (objectWrapper != null)
                return objectWrapper.getJsonObject();
        } catch (CacheException ce) {
            logger.error(String.format("Problem occurred while trying to retrieveSpecific from cache: %s", ce.getMessage()));
        }
        logger.error(String.format("No data found in cache."));
        return null;
    }

    public boolean isKeyPresent(String key){
        return objectCache.containsKey(key);
    }

}
