package pro.tremblay.ehcachequestions.stackoverflow.q45827753;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

/**
 * @author Henri Tremblay
 */
    public class ListenerObject implements CacheEventListener<String, String> {
      @Override
      public void onEvent(CacheEvent<? extends String, ? extends String> event) {
        System.out.println(event);
      }
    }
