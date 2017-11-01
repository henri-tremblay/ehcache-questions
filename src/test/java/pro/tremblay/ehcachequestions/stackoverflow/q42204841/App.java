package pro.tremblay.ehcachequestions.stackoverflow.q42204841;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.xml.XmlConfiguration;
import org.junit.Test;

import static org.ehcache.config.builders.CacheManagerBuilder.*;

/**
 * @author Henri Tremblay
 */
public class App {

    @Test
    public void test() {
        Configuration xmlConfig = new XmlConfiguration(App.class.getResource("ehcache.xml"));
        try (CacheManager cacheManager = newCacheManager(xmlConfig)) {
            cacheManager.init();

            Cache<String, String> basicCache = cacheManager.getCache("proc_req_cache", String.class, String.class);
        }
    }
}
