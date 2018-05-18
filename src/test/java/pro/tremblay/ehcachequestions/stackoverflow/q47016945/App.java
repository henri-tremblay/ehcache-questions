package pro.tremblay.ehcachequestions.stackoverflow.q47016945;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    MyService service = context.getBean(MyService.class);
    int count = service.getCountFromDB(1, 1);
    System.out.println(count);
    System.out.println(count);

    service.updateCounterCache(1, 60);
    count = service.getCountFromDB(1, 1);
    System.out.println(count);
    System.out.println(count);
  }

  @Bean
  public MyService myService() {
    return new MyService();
  }

  @Bean
  public JCacheManagerCustomizer cacheManagerCustomizer() {
    return cm -> {
      cm.createCache("countCache", new MutableConfiguration<>());
    };
  }
}
