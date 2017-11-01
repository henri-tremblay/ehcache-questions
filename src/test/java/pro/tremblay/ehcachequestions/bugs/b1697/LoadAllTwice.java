package pro.tremblay.ehcachequestions.bugs.b1697;

/**
 * 1477
 */

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import static org.ehcache.config.builders.CacheConfigurationBuilder.*;
import static org.ehcache.config.builders.CacheManagerBuilder.*;
import static org.ehcache.config.builders.ResourcePoolsBuilder.*;

public class LoadAllTwice {


@Test
public void test() {
    CacheLoaderWriter<Integer, String> loaderWriter = new CacheLoaderWriter<Integer, String>() {
        @Override
        public String load(Integer integer) throws Exception {
            return integer.toString();
        }

        @Override
        public Map<Integer, String> loadAll(Iterable<? extends Integer> iterable) {
            System.out.println("loadAll called");
            return StreamSupport.stream(iterable.spliterator(), false)
                .peek(i -> System.out.println("Id: " + i))
                .map(e -> new AbstractMap.SimpleImmutableEntry<>(e, e.toString()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        @Override
        public void write(Integer integer, String s) {
        }

        @Override
        public void writeAll(Iterable<? extends Map.Entry<? extends Integer, ? extends String>> iterable) {
        }

        @Override
        public void delete(Integer integer) throws Exception {
        }

        @Override
        public void deleteAll(Iterable<? extends Integer> iterable) {
        }
    };
    CacheManagerBuilder cacheManagerBuilder = newCacheManagerBuilder();
    try(CacheManager cacheManager = cacheManagerBuilder.build(true)) {
        Cache<Integer, String> cache = cacheManager.createCache("test",
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, String.class,
                ResourcePoolsBuilder.heap(10)).withLoaderWriter(loaderWriter).build());


        cache.getAll(IntStream.range(1, 10).mapToObj(i -> Integer.valueOf(i)).collect(Collectors.toSet()));
    }
}

}
