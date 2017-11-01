package pro.tremblay.ehcachequestions.stackoverflow.q44018771;

import org.springframework.boot.SpringApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Henri Tremblay
 */
  @Configuration
  @EnableCaching
  public class CacheConfig extends CachingConfigurerSupport {

    @Bean
    @Override
    public CacheManager cacheManager() {
      return  new EhCacheCacheManager(new net.sf.ehcache.CacheManager());
    }

    public static void main(String[] args) {
      SpringApplication app = new SpringApplication(CacheConfig.class);
      CacheManager cacheManager = app.run(args).getBean(CacheManager.class);
      System.out.println(cacheManager.getCache("languageCache"));
    }
  }
