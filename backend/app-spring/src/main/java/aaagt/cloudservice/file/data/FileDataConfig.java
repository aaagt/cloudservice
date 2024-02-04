package aaagt.cloudservice.file.data;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("aaagt.cloudservice.file.entity")
@EnableJpaRepositories("aaagt.cloudservice.file.repository")
public class FileDataConfig {
}
