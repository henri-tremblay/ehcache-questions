package pro.tremblay.ehcachequestions.stackoverflow.q50368760;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.PooledExecutionServiceConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * @author Henri Tremblay
 */
public class IterateTest {

  @Test
  public void test() {
    CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
      .build();
    Cache<String, Foo> cache = cacheManager.createCache("fooCache",
      CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Foo.class,
        ResourcePoolsBuilder.heap(20)).build());

    IntStream.range(0, 10).forEachOrdered(i -> cache.put("" + i, new Foo()));

    Map<String, Foo> foos = StreamSupport.stream(cache.spliterator(), false)
      .collect(Collectors.toMap(Cache.Entry::getKey, Cache.Entry::getValue));

    List<Cache.Entry<String, Foo>> foos2 = StreamSupport.stream(cache.spliterator(), false)
      .collect(Collectors.toList());


    List<Cache.Entry<String, Foo>> foos3 = new ArrayList<>();
    for(Cache.Entry<String, Foo> entry : cache) {
      foos3.add(entry);
    }
  }
}
