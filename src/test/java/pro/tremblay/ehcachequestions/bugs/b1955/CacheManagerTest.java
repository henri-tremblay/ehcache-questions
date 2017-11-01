package pro.tremblay.ehcachequestions.bugs.b1955;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Henri Tremblay
 */
public class CacheManagerTest {

  private static final String FRMLAB_CACHE_ALIAS = "alias";

  @Test
  public void test1() {
    CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
      .withCache(FRMLAB_CACHE_ALIAS,
        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Map.class,
          ResourcePoolsBuilder.newResourcePoolsBuilder()
            .heap(10L, MemoryUnit.MB))).build();
    cacheManager.init();

    Cache<String, Map> cache = cacheManager.getCache(FRMLAB_CACHE_ALIAS, String.class, Map.class);
    cache.put("allo", Collections.emptyMap());
    assertThat(cache.containsKey("allo")).isTrue();
  }

  @Test
  public void test2() {
    FormLabelCache formLabelCache = new FormLabelCache();
    formLabelCache.init();
    formLabelCache.addLables("1", Arrays.asList(f(1), f(2)));
    List<FormLabel> lbl = formLabelCache.getCodes("1", 1);
    assertThat(lbl).isNotNull();
  }

  private FormLabel f(int id) {
    return new FormLabel(id);
  }
}
