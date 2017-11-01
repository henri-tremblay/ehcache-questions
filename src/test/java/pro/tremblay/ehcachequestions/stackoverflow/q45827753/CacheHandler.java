package pro.tremblay.ehcachequestions.stackoverflow.q45827753;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.event.EventType;

public class CacheHandler implements AutoCloseable {
  private static final String CACHE_NAME = "basicCache";
  private final Cache<String, String> cache;
  private final CacheManager cacheManager;

  public CacheHandler() {
    cacheManager = initCacheManager();
    cache = cacheManager.getCache(CACHE_NAME, String.class, String.class);
  }

  private CacheManager initCacheManager(){
    CacheEventListenerConfigurationBuilder cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
      .newEventListenerConfiguration(new ListenerObject(), EventType.CREATED, EventType.UPDATED)
      .ordered().synchronous();

    return CacheManagerBuilder.newCacheManagerBuilder()
      .withCache(CACHE_NAME,
        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(10))
          .add(cacheEventListenerConfiguration)
      ).build(true);
  }

  public Cache getCache(){
    return cache;
  }

  @Override
  public void close() {
    cacheManager.close();
  }

  public static void main(String[] args) {
    try(CacheHandler handler = new CacheHandler()) {
      Cache<String, String> cache = handler.getCache();
      cache.put("a", "b");
      cache.putIfAbsent("a", "c");
    }

  }
}
