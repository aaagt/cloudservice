package aaagt.cloudservice;

import aaagt.cloudservice.fixture.CustomPostgreSQLContainer;
import aaagt.cloudservice.jwt.config.JwtProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("tc")
@EnableConfigurationProperties(value = JwtProperties.class)
class AppApplicationTests extends CustomPostgreSQLContainer {

    @Test
    void contextLoads() {
    }

}
