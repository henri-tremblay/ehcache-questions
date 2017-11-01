package pro.tremblay.ehcachequestions.stackoverflow.q43273264;

import org.ehcache.config.Configuration;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.ehcache.jsr107.config.Jsr107Configuration;
import org.ehcache.spi.service.ServiceCreationConfiguration;
import org.junit.Test;

import java.util.Collections;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

/**
 * @author Henri Tremblay
 */
public class DefaultTemplateTest {

  @Test
  public void test() {
    EhcacheCachingProvider provider = getCachingProvider();
    ServiceCreationConfiguration<?> jsr107config = new Jsr107Configuration("tiny", Collections.emptyMap(), true, null, null);
    Configuration config = new DefaultConfiguration(null, jsr107config);
    CacheManager cacheManager = provider.getCacheManager(provider.getDefaultURI(), config);
    Cache<String, String> cache = cacheManager.createCache("a", new MutableConfiguration<>());
//    for(int i = 0; i < 20; i++) {
//
//    }
//    cache.put("1",
  }

  private EhcacheCachingProvider getCachingProvider() {
    return (EhcacheCachingProvider) Caching.getCachingProvider();
  }
}
