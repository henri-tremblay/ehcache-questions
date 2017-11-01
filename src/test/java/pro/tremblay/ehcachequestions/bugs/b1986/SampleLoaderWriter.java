package pro.tremblay.ehcachequestions.bugs.b1986;

import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Henri Tremblay
 */
public class SampleLoaderWriter<K, V> implements CacheLoaderWriter<K, V> {

  private Map<K, V> values = new ConcurrentHashMap<>();

  public SampleLoaderWriter(Map<K, V> zero) {
    values.putAll(zero);
  }

  @Override
  public V load(K key) throws Exception {
    return values.get(key);
  }

  @Override
  public Map<K, V> loadAll(Iterable<? extends K> keys) throws BulkCacheLoadingException, Exception {
    return null;
  }

  @Override
  public void write(K key, V value) throws Exception {
    values.put(key, value);
  }

  @Override
  public void writeAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) throws BulkCacheWritingException, Exception {
    entries.forEach(e -> values.put(e.getKey(), e.getValue()));
  }

  @Override
  public void delete(K key) throws Exception {
    values.remove(key);
  }

  @Override
  public void deleteAll(Iterable<? extends K> keys) throws BulkCacheWritingException, Exception {
    keys.forEach(e -> values.remove(e));
  }
}
