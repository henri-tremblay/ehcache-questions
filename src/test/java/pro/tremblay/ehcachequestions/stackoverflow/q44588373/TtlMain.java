package pro.tremblay.ehcachequestions.stackoverflow.q44588373;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.ValueSupplier;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.expiry.Expiry;
import org.junit.Test;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.Soundbank;

import static org.junit.Assert.*;

/**
 * @author Henri Tremblay
 */
public class TtlMain {

  @Test
  public void test() throws InterruptedException {
    Expiry expiry = new Expiry() {
      @Override
      public Duration getExpiryForCreation(Object key, Object value) {
        long msUntilMidnight = LocalTime.now().until(LocalTime.of(13, 0), ChronoUnit.MILLIS);
        System.out.println(msUntilMidnight);
        msUntilMidnight = 100;
        return Duration.of(msUntilMidnight, TimeUnit.MILLISECONDS);
      }

      @Override
      public Duration getExpiryForAccess(Object key, ValueSupplier value) {
        return null;
      }

      @Override
      public Duration getExpiryForUpdate(Object key, ValueSupplier oldValue, Object newValue) {
        return null;
      }
    };

    try(CacheManager cm = CacheManagerBuilder.newCacheManagerBuilder()
      .withCache("cache",
        CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, String.class,
          ResourcePoolsBuilder.newResourcePoolsBuilder()
            .heap(10))
      .withExpiry(expiry))
          .build(true)) {

      // ...
    }
  }
}
