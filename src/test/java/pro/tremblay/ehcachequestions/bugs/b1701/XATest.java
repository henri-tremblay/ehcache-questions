package pro.tremblay.ehcachequestions.bugs.b1701;

import bitronix.tm.TransactionManagerServices;
import org.junit.Test;

import java.net.URI;

import javax.cache.Caching;

/**
 * @author Henri Tremblay
 */
public class XATest {

    @Test
    public void test() throws Exception {
        TransactionManagerServices.getTransactionManager();
        URI uri = getClass().getResource("ehcache.xml").toURI();
        Caching.getCachingProvider().getCacheManager(uri, getClass().getClassLoader());
    }

//    CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
//        .using(new LookupTransactionManagerProviderConfiguration(BitronixTransactionManagerLookup.class))
//        .withCache("xaCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
//            ResourcePoolsBuilder.heap(10))
//            .add(new XAStoreConfiguration("xaCache"))
//            .build()
//        )
//        .build(true);
//
//    final Cache<Long, String> xaCache = cacheManager.getCache("xaCache", Long.class, String.class);
//
//        transactionManager.begin();
//    {
//        xaCache.put(1L, "one");
//    }
//        transactionManager.commit();
//
//        cacheManager.close();
//        transactionManager.shutdown();

}
