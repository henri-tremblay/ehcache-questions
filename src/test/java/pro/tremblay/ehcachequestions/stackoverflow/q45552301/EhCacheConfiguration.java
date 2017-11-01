package pro.tremblay.ehcachequestions.stackoverflow.q45552301;

/**
 * @author Henri Tremblay
 */
import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;
import static org.ehcache.config.builders.ResourcePoolsBuilder.newResourcePoolsBuilder;

import java.util.concurrent.TimeUnit;

import org.ehcache.CacheManager;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class EhCacheConfiguration {

  private static final Logger log = LoggerFactory.getLogger(EhCacheConfiguration.class);

  @Bean
  public CacheManager ehCacheManager() {
    log.info("Creating cache manager programmatically");

    try(CacheManager cacheManager = newCacheManagerBuilder()
      .withCache("sessionCache",
        newCacheConfigurationBuilder(Long.class, String.class,
          newResourcePoolsBuilder().heap(2000, EntryUnit.ENTRIES).offheap(1, MemoryUnit.GB))
          .withExpiry(Expirations.timeToLiveExpiration(Duration.of(30, TimeUnit.MINUTES)))
          .withExpiry(Expirations.timeToIdleExpiration(Duration.of(5, TimeUnit.MINUTES))))
      .build(true)) {

      return cacheManager;
    }
  }
}
