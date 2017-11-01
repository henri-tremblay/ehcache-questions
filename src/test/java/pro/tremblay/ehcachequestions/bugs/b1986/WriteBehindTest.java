package pro.tremblay.ehcachequestions.bugs.b1986;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.PooledExecutionServiceConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.builders.WriteBehindConfigurationBuilder;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class WriteBehindTest {


  public static void main(final String[] args) throws URISyntaxException {
    WriteBehindTest test = new WriteBehindTest();
    test.testWriteBehind();
  }

  //@Test // it works from inside the junit test and open threadpools seam to be no problem
  public void testWriteBehind(){

    CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
      .using(PooledExecutionServiceConfigurationBuilder.newPooledExecutionServiceConfigurationBuilder()
        .defaultPool("dflt", 0, 10)
        .pool("defaultWriteBehindPool", 1, 3)
        .build())
      .withDefaultWriteBehindThreadPool("defaultWriteBehindPool") .build(true);

    Cache<Long, String> writeBehindCache = cacheManager.createCache("writeBehindCache",
      CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10))
        .withLoaderWriter(new SampleLoaderWriter<Long, String>(Collections.singletonMap(41L, "zero")))
        .add(WriteBehindConfigurationBuilder
          .newBatchedWriteBehindConfiguration(1, TimeUnit.SECONDS, 3)
          .queueSize(3)
          .concurrencyLevel(1)
          .enableCoalescing())
        .build());

    assertThat(writeBehindCache.get(41L), is("zero"));
    writeBehindCache.put(42L, "one");
    writeBehindCache.put(43L, "two");
    writeBehindCache.put(42L, "This goes for the record");
    assertThat(writeBehindCache.get(42L), equalTo("This goes for the record"));


    System.out.println("Closing");
    cacheManager.close();
    System.out.println("Closed");

  }
}
