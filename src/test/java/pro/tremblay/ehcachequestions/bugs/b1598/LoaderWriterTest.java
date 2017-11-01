package pro.tremblay.ehcachequestions.bugs.b1598;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.junit.Test;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author Henri Tremblay
 */
public class LoaderWriterTest {

  @Test
  public void test() throws InterruptedException {
    CacheConfigurationBuilder<Long, BigInteger> cacheConfiguration =
      CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, BigInteger.class,
        ResourcePoolsBuilder.newResourcePoolsBuilder()
          .heap(10)
          .disk(10, MemoryUnit.MB))
      .withExpiry(Expirations.timeToLiveExpiration(Duration.of(4, TimeUnit.SECONDS)));

    CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
      .withCache("aCache3", cacheConfiguration).build(true);

    Cache<Long, BigInteger> cache = cacheManager.getCache("aCache3", Long.class, BigInteger.class);

    assertEquals(cache.get(2L), BigInteger.ONE);
    Thread.sleep(6000);
    assertEquals(cache.get(2L), BigInteger.ONE); // fails here with null!=1
  }


}
