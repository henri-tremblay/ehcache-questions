package pro.tremblay.ehcachequestions.ml;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.store.CacheStore;
import net.sf.ehcache.store.disk.DiskStore;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author Henri Tremblay
 */
public class EvictionFromDisk {

  public static void main(String[] args) throws Exception {
    CacheManager cm = CacheManager.create(EvictionFromDisk.class.getResource("/ehcache-disk.xml"));

    cm.addCache("cache");

    Cache cache = cm.getCache("cache");

    insert(cache, "A", 2);
    insert(cache, "B", 2);
    insert(cache, "C", 2);
    insert(cache, "D", 2);
    insert(cache, "E", 2);
    insert(cache, "F", 2);
    insert(cache, "G", 2);
    insert(cache, "H", 100); // disk start filling here
    insert(cache, "I", 2);
    insert(cache, "J", 100);

//    flushStore(cache);
//    cache.flush();
//    Thread.sleep(2000);
//    DiskStoreHelper.flushAllEntriesToDisk(cache).get();
    insert(cache, "K", 100); // eviction kicks in here
    insert(cache, "L", 100);
    insert(cache, "M", 100);
    insert(cache, "N", 100);
    insert(cache, "O", 100);

    cm.shutdown();
  }

  private static void insert(Cache cache, String letter, int size) {
    Object o = cache.getStoreMBean();
    System.out.println("Adding " + letter);
    System.out.println("Keys before: " + cache.getKeys().stream().sorted().collect(Collectors.joining(",")));
    System.out.println("Size before: " + new File("logs/cache/cache.data").length());
    cache.put(new Element("Item" + letter, new byte[size * 1024 * 1024]));
    System.out.println("Keys after: " + cache.getKeys().stream().sorted().collect(Collectors.joining(",")));
    System.out.println("Size after: " + new File("logs/cache/cache.data").length());
  }

}
