package pro.tremblay.ehcachequestions.stackoverflow.q45665733;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.units.MemoryUnit;
import org.junit.Test;

import static org.ehcache.config.builders.CacheConfigurationBuilder.*;
import static org.ehcache.config.builders.CacheManagerBuilder.*;
import static org.ehcache.config.builders.ResourcePoolsBuilder.*;
import static org.junit.Assert.*;

/**
 * @author Henri Tremblay
 */
public class SerializerTest {

    @Test
    public void test() {
      try(CacheManager persistentCacheManager =
            newCacheManagerBuilder()
              .withCache("test-cache",
                newCacheConfigurationBuilder(Integer.class, MyClass.class,
                  newResourcePoolsBuilder().offheap(1, MemoryUnit.MB)))
              .withSerializer(MyClass.class, MySerializer.class)
              .build(true)) {

        Cache<Integer, MyClass> cache = persistentCacheManager.getCache("test-cache", Integer.class, MyClass.class);
        cache.put(1, new MyClass("test"));
        MyClass actual = cache.get(1);
        assertEquals("test", actual.getValue());
      }

    }

}
