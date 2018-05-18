package pro.tremblay.ehcachequestions.stackoverflow.q47027574;

import pro.tremblay.ehcachequestions.stackoverflow.q45665733.MyClass;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import javax.cache.configuration.MutableConfiguration;

/**
 * @author Henri Tremblay
 */
@Configuration
@EnableAutoConfiguration
@EnableCaching
public class App {

  public static void main(String[] args) throws InterruptedException {
    ApplicationContext context = new AnnotationConfigApplicationContext(App.class);

    MyService app = context.getBean(MyService.class);
    app.cacheMeta("a", "b", new CachedObject());

    CacheManager cacheManager = context.getBean(CacheManager.class);
    Cache cache = cacheManager.getCache("cacheOne");

    Cache.ValueWrapper wrapper = cache.get("a".hashCode() + "" + "b".hashCode());

    System.out.println(wrapper.get());
  }

  @Bean
  public MyService myService() {
    return new MyService();
  }

  @Bean
  public KeyGenerator keyGenerator() {
    return new KeyGenerator() {
      @Override
      public Object generate(Object target, Method method, Object... params) {
        return params[0].hashCode() + "" + params[1].hashCode();
      }
    };
  }

  @Bean
  public JCacheManagerCustomizer cacheManagerCustomizer() {
    return cm -> {
      cm.createCache("cacheOne", new MutableConfiguration<>());
    };
  }
}
