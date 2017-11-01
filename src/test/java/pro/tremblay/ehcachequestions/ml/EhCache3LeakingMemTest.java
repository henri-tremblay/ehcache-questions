package pro.tremblay.ehcachequestions.ml;

import java.lang.management.BufferPoolMXBean;
import java.util.concurrent.TimeUnit;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.PooledExecutionServiceConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.EhcacheManager;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.junit.Ignore;
import org.junit.Test;
import sun.management.ManagementFactoryHelper;

public class EhCache3LeakingMemTest {

    private CacheManager cacheManager;

    public static void main(String[] args) throws Exception {
        EhCache3LeakingMemTest e = new EhCache3LeakingMemTest();
        e.test();
    }

    @Test
    @Ignore
    public void test() throws Exception {
        System.out.println("START");
        logMappedBufferPoolStats(0);

        initCacheManager();

        System.out.println("CACHE INITIALIZED");
        logMappedBufferPoolStats(0);

        int noCacheToCreate = 4000;
        //create 10000 caches
        for (int i = 1; i <= noCacheToCreate; i++) {
            //build cache
            Cache<Integer, String> c = buildCache("CACHE[" + i + "]");

            //fill it with 100 items
            for (int k = 1; k <= 100; k++) {
                c.put(k, k + "___SAMPLE");
            }

            //measure buffer pools every 10th cache
            //THERE ARE 16 BUFFERS CREATED PER SINGLE CACHE !!!!
            if (i % 10 == 0) {
                logMappedBufferPoolStats(i);
            }
        }

        System.out.println();
        System.out.println("WAIT A WHILE FOR THREADS TO STORE DATA TO DISK AND RELEASE BUFFER POOLS (HOPEFULLY)");
        System.out.println("THESE BUFFER POOLS ARE NOT RELEASED CAUSING JVM CRASH WITH MORE THAN 4000 CACHES CREATED");
        System.out.println();
        Thread.sleep(10000);
        logMappedBufferPoolStats(noCacheToCreate);

        Thread.currentThread().join();
    }

    private void initCacheManager() {
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
            .using(
                PooledExecutionServiceConfigurationBuilder
                    .newPooledExecutionServiceConfigurationBuilder()
                    .defaultPool("defaultPool", 1, 10)
                    .build()
            )
            .with(CacheManagerBuilder.persistence("/tmp/cache"))
            .withDefaultDiskStoreThreadPool("defaultPool")
            .build();
        cacheManager.init();
    }

    private Cache<Integer, String> buildCache(String name) {
        ResourcePoolsBuilder resourcePoolsBuilder = ResourcePoolsBuilder
            .newResourcePoolsBuilder()
            .heap(5, EntryUnit.ENTRIES)
            .disk(1, MemoryUnit.MB, false);

        CacheConfiguration<Integer, String> cacheConfiguration = CacheConfigurationBuilder
            .newCacheConfigurationBuilder(Integer.class, String.class, resourcePoolsBuilder)
            .withDiskStoreThreadPool("defaultPool", 3)
            .withExpiry(Expirations.timeToLiveExpiration(Duration.of(3600, TimeUnit.SECONDS)))
            .build();
        return cacheManager.createCache(name, cacheConfiguration);
    }

    private void logMappedBufferPoolStats(int cacheCount) {
        for (BufferPoolMXBean mxBean : ManagementFactoryHelper.getBufferPoolMXBeans()) {
            if (mxBean.getName().equals("mapped")) {
                long count = mxBean.getCount();
                long memoryUsed = mxBean.getMemoryUsed();
                long totalCapacity = mxBean.getTotalCapacity();
                System.out.println("BUFFER POOL size:" + count + ", memoryUsed:" + memoryUsed + ", totalCapacity:" + totalCapacity);
                if (cacheCount != 0) {
                    System.out.println("JVM mapped bufferPool:" + count + ", cacheCount: " + cacheCount + ", bufferpools per cache:" + count / cacheCount);
                }
            }
        }
    }
}
