package pro.tremblay.ehcachequestions.bugs.tdb4444;

import java.io.ByteArrayInputStream;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PinningConfiguration;
import net.sf.ehcache.config.PinningConfiguration.Store;

public class DynamicCacheMemoryLeak {

  public static void main(String[] args) throws Exception {
    final String cacheName = "testcache";
    final String ehcacheXml = "<ehcache name='dynamiccache' maxBytesLocalHeap='100M'/>";

    CacheManager cacheManager = CacheManager.newInstance(new ByteArrayInputStream(ehcacheXml.getBytes()));

    for (long i = 1; ; ++i) {

      Ehcache cache = cacheManager.getEhcache(cacheName);
      if (cache != null) {
        cache.removeAll();
        cacheManager.removeCache(cacheName);
      }

      CacheConfiguration configuration = new CacheConfiguration()
        .name(cacheName)
        .eternal(true)
        .pinning(new PinningConfiguration().store(Store.INCACHE));
      cache = cacheManager.addCacheIfAbsent(new Cache(configuration));
      cache.put(new Element(42, "hello world"));

      System.err.println(i);
    }
  }
}
