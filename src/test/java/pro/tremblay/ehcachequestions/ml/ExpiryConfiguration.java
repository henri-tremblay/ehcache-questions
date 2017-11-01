package pro.tremblay.ehcachequestions.ml;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.expiry.Expiry;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.ehcache.config.builders.CacheManagerBuilder.*;

/**
 * @author Henri Tremblay
 */
public class ExpiryConfiguration {

  @Test
  public void test() {
    CacheConfiguration<Long, String> cacheConfiguration =
      CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
      ResourcePoolsBuilder.heap(100))
      .withExpiry(Expirations.timeToLiveExpiration(Duration.of(20, TimeUnit.SECONDS)))
      .build();

    try(CacheManager persistentCacheManager =
          newCacheManagerBuilder()
            .withCache("test-cache", cacheConfiguration).build(true)) {

      Cache cache = persistentCacheManager.getCache("test-cache", Long.class, String.class);

      Expiry o = cache.getRuntimeConfiguration().getExpiry();
      Duration d = o.getExpiryForCreation(null, null);
      System.out.println(d);
    }
  }
}
