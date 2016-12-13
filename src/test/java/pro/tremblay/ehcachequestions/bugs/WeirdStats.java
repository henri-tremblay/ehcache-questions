package pro.tremblay.ehcachequestions.bugs;

import pro.tremblay.ehcachequestions.utils.Utils;
import org.ehcache.Cache;
import org.ehcache.CacheManager;

import java.net.URI;

public class WeirdStats {

    private static final URI clusterURI = URI.create("terracotta://localhost:9510");
    private static final String cacheName = "weird";

    public static void main(String[] args) throws Exception {

        try (CacheManager cacheManager = Utils.getLocalCacheManager(cacheName)) {

            Cache<Long, String> cache = cacheManager.getCache(cacheName, Long.class, String.class);

            for(long key = 0; key < 10; key++) {
                System.out.println(key);
                Utils.printMetrics(cacheName);
                cache.put(key, "xxxx"); // out
                Utils.printMetrics(cacheName);
                cache.get(key); // cache hit
                Utils.printMetrics(cacheName);
                cache.get(1231241235L); // cache miss
                Utils.printMetrics(cacheName);
            }

            System.out.println("First part done");
            Utils.printMetrics(cacheName);
        }

        try (CacheManager cacheManager = Utils.getLocalCacheManager(cacheName)) {
            Cache<Long, String> cache = cacheManager.getCache(cacheName, Long.class, String.class);
            Utils.printMetrics(cacheName);
        }
    }

//
//    public static EhcacheManager createCacheManager(URI clusterURI, boolean autoCreate, String cacheName, long cacheSize, MemoryUnit unit, Consistency consistency) {
//        ClusteringServiceConfigurationBuilder clusteringServiceConfigurationBuilder = ClusteringServiceConfigurationBuilder.cluster(clusterURI);
//        ServerSideConfigurationBuilder serverSideConfigurationBuilder = (autoCreate ? clusteringServiceConfigurationBuilder.autoCreate() : clusteringServiceConfigurationBuilder.expecting())
//            .defaultServerResource("primary-server-resource");
//
//        CacheManagerBuilder<PersistentCacheManager> cacheManagerBuilder =
//            CacheManagerBuilder.newCacheManagerBuilder()
//                .with(serverSideConfigurationBuilder)
//                .withCache(cacheName, CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, byte[].class,
//                    ResourcePoolsBuilder.newResourcePoolsBuilder()
//                        .with(ClusteredResourcePoolBuilder.clusteredDedicated(cacheSize, unit))
//                ).add(new ClusteredStoreConfiguration(consistency)));
//
//        Configuration conf = extractConfiguration(cacheManagerBuilder);
//        EhcacheCachingProvider provider = (EhcacheCachingProvider)Caching.getCachingProvider();
//
//        return provider.getCacheManager(URI.create("file://ehcache.xml"), conf).unwrap(EhcacheManager.class);
//    }
}
