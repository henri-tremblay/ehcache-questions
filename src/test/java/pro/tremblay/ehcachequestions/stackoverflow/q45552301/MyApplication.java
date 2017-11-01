package pro.tremblay.ehcachequestions.stackoverflow.q45552301;

/**
 * @author Henri Tremblay
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication
public class MyApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(MyApplication.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(MyApplication.class, args);
  }
}
