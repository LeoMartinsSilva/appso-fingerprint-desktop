package finger.jetty;

import java.awt.AWTException;
import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@Import(AppConfig.class)
public class CustomApplication {
	static MyTrayIcon icon;
	
    public static void main(String[] args) {
    	SpringApplicationBuilder builder = new SpringApplicationBuilder(CustomApplication.class);
        builder.headless(false);
    	
         try {
			icon = new MyTrayIcon();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        SpringApplication app = builder.build();
        app.setDefaultProperties(Collections
          .singletonMap("server.port", "9001"));
        app.run(args);
        
        
    }
    
    @PostConstruct
    private void setup() throws AWTException{
        
        icon.iniciarTray();
    }
}
