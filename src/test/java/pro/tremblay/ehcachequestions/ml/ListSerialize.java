package pro.tremblay.ehcachequestions.ml;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Henri Tremblay
 */
public class ListSerialize {

  private static final String CACHE_NAME = "cache";

  @Test
  public void test() {
    try(CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
      .withCache(CACHE_NAME,
        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, List.class, ResourcePoolsBuilder.heap(10))
          .withExpiry(Expirations.timeToLiveExpiration(Duration.of(2, TimeUnit.HOURS)))
      ).build(true)) {

    }
  }
}
