package pro.tremblay.ehcachequestions.stackoverflow.q41520778;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;

import java.net.URL;

/**
 * @author Henri Tremblay
 */
public class CheckJmx {

    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = CheckJmx.class.getClassLoader();
        URL url = classLoader.getResource("pro/tremblay/ehcachequestions/stackoverflow/ehcache.xml");
        XmlConfiguration xmlConfig = new XmlConfiguration(url);
        try(CacheManager myCacheManager = CacheManagerBuilder.newCacheManager(xmlConfig)) {
            myCacheManager.init();
            Thread.sleep(60_000);
        }
    }
}
