package pro.tremblay.ehcachequestions.experience;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.ResourceType;
import org.ehcache.config.SizedResourcePool;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.config.SizedResourcePoolImpl;

import static org.ehcache.config.builders.CacheConfigurationBuilder.*;
import static org.ehcache.config.builders.CacheManagerBuilder.*;

/**
 * @author Henri Tremblay
 */
public class SharedPool {

    public static void main(String[] args) {
        try(CacheManager cacheManager = disk()) {

            Cache<Integer, String> cache1 = cacheManager.getCache("test-cache1", Integer.class, String.class);
            Cache<Integer, String> cache2 = cacheManager.getCache("test-cache2", Integer.class, String.class);

            for(int i = 0; i < 10; i++) {
                cache1.put(i, "" + i);
                cache2.put(i+10, "" + (i+10));
            }

            for(int i = 0; i < 10; i++) {
                System.out.println(cache1.get(i));
                System.out.println(cache2.get(i+10));
            }

        }
    }

    private static CacheManager disk() {
        ResourcePools pool = ResourcePoolsBuilder.newResourcePoolsBuilder().disk(10, MemoryUnit.MB).build();

        return CacheManagerBuilder.newCacheManagerBuilder()
            .with(CacheManagerBuilder.persistence("target/file.txt"))
            .withCache("test-cache1", newCacheConfigurationBuilder(Integer.class, String.class, pool))
            .withCache("test-cache2", newCacheConfigurationBuilder(Integer.class, String.class, pool)).build(true);
    }

    public static CacheManager foo() {
        ResourcePools pool = ResourcePoolsBuilder.newResourcePoolsBuilder().offheap(10, MemoryUnit.MB).heap(10).build();

        return newCacheManagerBuilder()
            .withCache("test-cache1", newCacheConfigurationBuilder(Integer.class, String.class, pool))
            .withCache("test-cache2", newCacheConfigurationBuilder(Integer.class, String.class, pool))
            .build(true);
    }

    public static void bar() {
        SizedResourcePool pool = new SizedResourcePoolImpl<SizedResourcePool>(ResourceType.Core.HEAP, 10, EntryUnit.ENTRIES, false);

        ResourcePools pools = ResourcePoolsBuilder.newResourcePoolsBuilder()
            .with(pool)
            .offheap(10, MemoryUnit.MB)
            .build();

        CacheManager cacheManager = newCacheManagerBuilder()
            .withCache("test-cache1", newCacheConfigurationBuilder(Integer.class, String.class, pools))
            .withCache("test-cache2", newCacheConfigurationBuilder(Integer.class, String.class, pools))
            .build(true);
    }
}
