package ;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.example.config.GracefulShutdown;

@SpringBootApplication
@RefreshScope
@Configuration
public class Applicationexample extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(Applicationexample.class, args);
	}
	
	
	@Bean
	public GracefulShutdown gracefulShutdown() {
	    return new GracefulShutdown();
	}
	@Bean
	public ConfigurableServletWebServerFactory webServerFactory(final GracefulShutdown gracefulShutdown) {
	    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
	    factory.addConnectorCustomizers(gracefulShutdown);
	    return factory;
	}
	
	 public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
	        taskRegistrar.setScheduler(taskExecutor());
	    }

	    @Bean(destroyMethod = "shutdownNow")
	    public Executor taskExecutor() {
	        return Executors.newScheduledThreadPool(100);
	    }

}
