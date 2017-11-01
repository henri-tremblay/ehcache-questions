package pro.tremblay.ehcachequestions.bugs.b1714;

import org.ehcache.PersistentCacheManager;
import org.ehcache.StateTransitionException;
import org.ehcache.clustered.client.config.ClusteredStoreConfiguration;
import org.ehcache.clustered.client.config.builders.ClusteredResourcePoolBuilder;
import org.ehcache.clustered.client.config.builders.ClusteringServiceConfigurationBuilder;
import org.ehcache.clustered.client.config.builders.ServerSideConfigurationBuilder;
import org.ehcache.clustered.common.Consistency;
import org.ehcache.clustered.common.internal.exceptions.InvalidOperationException;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.*;

/**
 * 1714
 *
 * @author Henri Tremblay
 */
public class AlreadyExists {

    @Test
    public void test() {
        try {
            createCacheManager(10);
        } catch(StateTransitionException e) {
            assertEquals(InvalidOperationException.class, getRootCause(e).getClass());
        }
        createCacheManager(100);
    }

    private static PersistentCacheManager createCacheManager(long size) {
        ClusteringServiceConfigurationBuilder clusteringServiceConfigurationBuilder = ClusteringServiceConfigurationBuilder.cluster(URI.create("terracotta://localhost:9510"));
        ServerSideConfigurationBuilder serverSideConfigurationBuilder = clusteringServiceConfigurationBuilder.autoCreate()
            .defaultServerResource("primary-server-resource");

        CacheManagerBuilder<PersistentCacheManager> cacheManagerBuilder =
            CacheManagerBuilder.newCacheManagerBuilder()
                .with(serverSideConfigurationBuilder)
                .withCache("test-cache", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                    ResourcePoolsBuilder.newResourcePoolsBuilder()
                        .with(ClusteredResourcePoolBuilder.clusteredDedicated(size, MemoryUnit.KB))
                ).add(new ClusteredStoreConfiguration(Consistency.EVENTUAL)));

        return cacheManagerBuilder.build(true);
    }

    private static Throwable getRootCause(Throwable e) {
        Throwable current = e;
        while(current.getCause() != null) {
            current = current.getCause();
        }
        return current;
    }
}
