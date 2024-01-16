package aaagt.cloudservice.app;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@SpringBootApplication(scanBasePackages = "aaagt.cloudservice")
@EntityScan("aaagt.cloudservice")
@EnableJpaRepositories("aaagt.cloudservice")
@ComponentScan(basePackages = {"aaagt.cloudservice"})
@EnableConfigurationProperties
@ConfigurationPropertiesScan({"aaagt.cloudservice"})
@RestController
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @GetMapping("/")
    public String home() {
        log.info("GET /home");
        return "hello";
    }

}
