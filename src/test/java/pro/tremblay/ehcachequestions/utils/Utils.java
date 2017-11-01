package pro.tremblay.ehcachequestions.utils;

import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.clustered.client.config.ClusteredStoreConfiguration;
import org.ehcache.clustered.client.config.builders.ClusteredResourcePoolBuilder;
import org.ehcache.clustered.client.config.builders.ClusteringServiceConfigurationBuilder;
import org.ehcache.clustered.client.config.builders.ServerSideConfigurationBuilder;
import org.ehcache.clustered.common.Consistency;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.EhcacheCachingProvider;

import java.lang.management.ManagementFactory;
import java.net.URI;

import javax.cache.Caching;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import static org.ehcache.config.builders.CacheConfigurationBuilder.*;
import static org.ehcache.config.builders.CacheManagerBuilder.*;
import static org.ehcache.config.builders.ResourcePoolsBuilder.*;

public final class Utils {

    private static class Metrics {
        long evictions;
        long gets;
        long hits;
        long misses;
        long puts;
        float getTime;
        float putTime;

        @Override
        public String toString() {
            return String.format("%d evictions, %d gets, %d puts, %d hits, %d misses, %f getTime, %f putTime",
                evictions, gets, puts, hits, misses, getTime, putTime);
        }

        public Metrics getDelta(Metrics previousMetrics) {
            Metrics metrics = new Metrics();
            metrics.evictions = evictions - previousMetrics.evictions;
            metrics.gets = gets - previousMetrics.gets;
            metrics.hits = hits - previousMetrics.hits;
            metrics.misses = misses - previousMetrics.misses;
            metrics.puts = puts - previousMetrics.puts;
            metrics.getTime = getTime - previousMetrics.getTime;
            metrics.putTime = putTime - previousMetrics.putTime;
            return metrics;
        }
    }

    private static final String M_BEAN_COORDINATES = "javax.cache:type=CacheStatistics,CacheManager=file.//ehcache.xml,Cache=";
    private static final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

    private Utils() {}

    public static ObjectName getObjectName(String cacheName) {
        try {
            return ObjectName.getInstance(M_BEAN_COORDINATES + cacheName);
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        }
    }

    public static long getEvictions(ObjectName objectName) {
        return (long) getAttribute(objectName, "CacheEvictions");
    }

    public static long getCacheGets(ObjectName objectName) {
        return (long) getAttribute(objectName, "CacheGets");
    }

    public static long getCacheHits(ObjectName objectName) {
        return (long) getAttribute(objectName, "CacheHits");
    }

    public static long getCacheMisses(ObjectName objectName) {
        return (long) getAttribute(objectName, "CacheMisses");
    }

    public static long getCachePuts(ObjectName objectName) {
        return (long) getAttribute(objectName, "CachePuts");
    }

    public static float getAverageGetTime(ObjectName objectName) {
        return (float) getAttribute(objectName, "AverageGetTime");
    }

    public static float getAveragePutTime(ObjectName objectName) {
        return (float) getAttribute(objectName, "AveragePutTime");
    }

    private static Object getAttribute(ObjectName objectName, String attributeName) {
        try {
            return mBeanServer.getAttribute(objectName, attributeName);
        } catch (JMException e) {
            System.err.println("Failed to retrieve attribute " + e);
            return 0;
        }
    }

    private static Metrics getMetrics(ObjectName name) {
        Metrics metrics = new Metrics();
        metrics.evictions = Utils.getEvictions(name);
        metrics.gets = Utils.getCacheGets(name);
        metrics.hits = Utils.getCacheHits(name);
        metrics.misses = Utils.getCacheMisses(name);
        metrics.puts = Utils.getCachePuts(name);
        metrics.getTime = Utils.getAverageGetTime(name);
        metrics.putTime = Utils.getAveragePutTime(name);
        return metrics;
    }

    private static void printMetrics(ObjectName name) {
        Metrics metrics = getMetrics(name);
        System.out.println(metrics);
    }

    public static void printMetrics(String cacheName) {
        ObjectName name = Utils.getObjectName(cacheName);
        printMetrics(name);
    }

    public static javax.cache.CacheManager getLocalCacheManager(String cacheName) {
        CacheManagerBuilder<CacheManager> cacheManagerBuilder = newCacheManagerBuilder()
            .withCache(cacheName,
                newCacheConfigurationBuilder(
                    Long.class, String.class,
                    newResourcePoolsBuilder()
                        .heap(1, EntryUnit.ENTRIES)
                )
            );

        Configuration conf = extractConfiguration(cacheManagerBuilder);
        EhcacheCachingProvider provider = (EhcacheCachingProvider) Caching.getCachingProvider();

        return provider.getCacheManager(URI.create("file://ehcache.xml"), conf);
    }

    public static javax.cache.CacheManager getClusteredCacheManager(String cacheName) {
        ClusteringServiceConfigurationBuilder clusteringServiceConfigurationBuilder = ClusteringServiceConfigurationBuilder.cluster(URI.create("terracotta://localhost:9510"));
        ServerSideConfigurationBuilder serverSideConfigurationBuilder = clusteringServiceConfigurationBuilder.autoCreate()
            .defaultServerResource("primary-server-resource");

        CacheManagerBuilder<PersistentCacheManager> cacheManagerBuilder =
            CacheManagerBuilder.newCacheManagerBuilder()
                .with(serverSideConfigurationBuilder)
                .with(persistence("target/cache"))
                .withCache(cacheName, CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                    ResourcePoolsBuilder.newResourcePoolsBuilder()
                        .with(ClusteredResourcePoolBuilder.clusteredDedicated(100, MemoryUnit.KB))
                ).add(new ClusteredStoreConfiguration(Consistency.EVENTUAL)));

        Configuration conf = extractConfiguration(cacheManagerBuilder);
        EhcacheCachingProvider provider = (EhcacheCachingProvider) Caching.getCachingProvider();

        return provider.getCacheManager(URI.create("file://ehcache.xml"), conf);
    }

    private static Configuration extractConfiguration(CacheManagerBuilder<? extends CacheManager> cacheManagerBuilder) {
        CacheManager persistentCacheManager = cacheManagerBuilder.build();
        return persistentCacheManager.getRuntimeConfiguration();
    }
}
