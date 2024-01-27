package aaagt.cloudservice.jwt.service.impl;

import aaagt.cloudservice.jwt.config.JwtConfig;
import aaagt.cloudservice.jwt.config.JwtProperties;
import aaagt.cloudservice.jwt.dto.JwtPayloadDto;
import aaagt.cloudservice.jwt.service.JwtService;
import com.auth0.jwt.JWT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = {JwtConfig.class})
@EnableConfigurationProperties(value = {JwtProperties.class})
class JwtServiceImplTest {

    private static final Logger log = Logger.getLogger(JwtServiceImplTest.class.getName());

    @Autowired
    JwtService jwtService;

    @Test
    void generateToken() {
        long milliseconds = 999999000;
        Instant issuedAt = Instant.ofEpochMilli(milliseconds);
        Instant expireAt = Instant.ofEpochMilli(milliseconds)
                .plus(Duration.ofMillis(10_000L));
        var payload = new JwtPayloadDto(
                "test subject",
                Optional.of(issuedAt),
                Optional.of(expireAt));
        var token = jwtService.generateToken(payload);
        var expected = "test subject";
        log.info("Token: " + token);
        assertEquals(expected, JWT.decode(token).getSubject());
    }

}
