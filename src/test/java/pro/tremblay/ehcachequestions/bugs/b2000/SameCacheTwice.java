package pro.tremblay.ehcachequestions.bugs.b2000;

import org.ehcache.CacheManager;
import org.ehcache.clustered.client.config.builders.ClusteredResourcePoolBuilder;
import org.ehcache.clustered.client.config.builders.ClusteringServiceConfigurationBuilder;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.junit.Test;

import java.net.URI;

import static org.ehcache.config.builders.ResourcePoolsBuilder.*;

/**
 * @author Henri Tremblay
 */
public class SameCacheTwice {

  @Test
  public void test() {
    String cacheAlias = "cacheAliasSameName";

    CacheConfiguration<Long, String> cacheConfig = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, newResourcePoolsBuilder()
      .heap(10))
      .build();

    try(CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
      .withCache(cacheAlias, cacheConfig)
      .withCache(cacheAlias, cacheConfig)
      .build(true)) {

//      cacheManager.createCache(cacheAlias, cacheConfig);
    }
  }
}
