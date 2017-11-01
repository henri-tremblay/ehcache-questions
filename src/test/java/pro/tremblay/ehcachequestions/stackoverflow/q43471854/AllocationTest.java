package pro.tremblay.ehcachequestions.stackoverflow.q43471854;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration;
import org.junit.Test;

import java.io.File;
import java.io.Serializable;

/**
 * @author Henri Tremblay
 */
public class AllocationTest implements Serializable {

  @Test
  public void test() {
    CacheManager cacheManager = CacheManagerBuilder
      .newCacheManagerBuilder()
      .with(new CacheManagerPersistenceConfiguration(new File("target/cache", "DictionaryCache")))
      .withCache("dictionaryCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, AllocationTest.class,
        ResourcePoolsBuilder.newResourcePoolsBuilder().disk(200, MemoryUnit.MB).heap(20, EntryUnit.ENTRIES))
        .build()).build(true);
  }
}
