package pro.tremblay.ehcachequestions.bugs.b1598;

import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Henri Tremblay
 */
public class MapCacheLoaderWriter implements CacheLoaderWriter<Long, BigInteger> {
  private Logger LOGGER = LoggerFactory.getLogger(getClass());

  private Map<Long, BigInteger> map;

  public  MapCacheLoaderWriter() {
    map = new HashMap<Long, BigInteger>();
  }

  @Override
  public void delete(Long k) throws Exception { }

  @Override
  public void deleteAll(Iterable<? extends Long> ks) {    }

  @Override
  public BigInteger load(Long k) throws Exception {
    LOGGER.info("Cache load: " + k);
    if (!map.containsKey(k)) {
      BigInteger v = fib(k);
      map.put(k, v);
    }
    return map.get(k);
  }

  @Override
  public Map<Long, BigInteger> loadAll(Iterable<? extends Long> ks) throws Exception {
    Map<Long, BigInteger> result = new HashMap<Long, BigInteger>();
    for (Long k : ks) {
      load(k);
    }
    return result;
  }

  @Override
  public void write(Long k, BigInteger v) {
    LOGGER.info("Cache write: " + k + " " + v);
    map.put(k, v);
  }

  @Override
  public void writeAll(Iterable<? extends Map.Entry<? extends Long, ? extends BigInteger>> kvMap) {
    for (Map.Entry<? extends Long, ? extends BigInteger> k : kvMap) {
      write(k.getKey(), k.getValue());
    }
  }

  private BigInteger fib(long k) {
    // some code returning a non-null value
    return BigInteger.ONE;
  }
}
