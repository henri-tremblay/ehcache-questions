package pro.tremblay.ehcachequestions.bugs.b1477;

/**
 * 1477
 */

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.units.EntryUnit;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.ehcache.config.builders.CacheConfigurationBuilder.*;
import static org.ehcache.config.builders.CacheManagerBuilder.*;
import static org.ehcache.config.builders.ResourcePoolsBuilder.*;

public class OffheapAllocation {


    @Test
    public void testPool() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < 100_000; i++) {

            CacheManager cacheManager = createCacheManager();

            Cache<Long, byte[]> cache = cacheManager.getCache("cache", Long.class, byte[].class);
            cache.put(1L, new byte[1000]);

//            System.gc();
//            System.gc();
//            System.gc();

//            StatisticsManager statisticsManager = new StatisticsManager();
//            statisticsManager.root(cacheManager);
//
//            Query query = queryBuilder().descendants().filter(context(attributes(hasAttribute("name", "mappings")))).build();
//            TreeNode node = statisticsManager.queryForSingleton(query);

            cacheManager.close();
//            latch.await();
        }
    }

    private CacheManager createCacheManager() {
        CacheManagerBuilder cacheManagerBuilder = newCacheManagerBuilder();
        return cacheManagerBuilder
            .withCache("cache", newCacheConfigurationBuilder(Long.class, byte[].class, newResourcePoolsBuilder()
                .heap(1, EntryUnit.ENTRIES)))
            .build(true);
    }

}
