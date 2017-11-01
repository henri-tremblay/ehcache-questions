package pro.tremblay.ehcachequestions.stackoverflow.q42057073;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.xml.XmlConfiguration;

import static org.ehcache.config.builders.CacheManagerBuilder.*;

/**
 * @author Henri Tremblay
 */
public class BaseXML {

    public static void main(String[] args) throws Exception {
        Configuration xmlConfig = new XmlConfiguration(BaseXML.class.getResource("ehcache.xml"));
        try (CacheManager cacheManager = newCacheManager(xmlConfig)) {
            cacheManager.init();

            Cache<String, PDFTO> basicCache = cacheManager.getCache("pdfCache", String.class, PDFTO.class);
            byte[] buffer = new byte[12 * 1024 * 1024];
            for(int i = 0; i < 10_000; i++) {
                basicCache.put("" + i, new PDFTO("rib.pdf", buffer));
            }
        }
    }
}
