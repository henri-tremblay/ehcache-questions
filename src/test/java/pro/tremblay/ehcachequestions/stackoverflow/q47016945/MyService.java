package pro.tremblay.ehcachequestions.stackoverflow.q47016945;

import pro.tremblay.ehcachequestions.stackoverflow.q47027574.CachedObject;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Henri Tremblay
 */
@Component
public class MyService {

  private int count = 42;

  @Cacheable(cacheNames="countCache", key="#id")
  public int getCountFromDB(int id, int length) {
    return count++;
  }

  @CachePut(value = "countCache", key = "#id")
  public long updateCounterCache(int id, long count) {
    return count;
  }

}
