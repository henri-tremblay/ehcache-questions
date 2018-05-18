package pro.tremblay.ehcachequestions.stackoverflow.q47027574;

import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

/**
 * @author Henri Tremblay
 */
@Component
public class MyService {
  @CachePut(value = "cacheOne", keyGenerator = "keyGenerator")
  public CachedObject cacheMeta(final Object obj1,
                                final Object obj2,
                                final CachedObject cachedObject) {
    return cachedObject;
  }
}
