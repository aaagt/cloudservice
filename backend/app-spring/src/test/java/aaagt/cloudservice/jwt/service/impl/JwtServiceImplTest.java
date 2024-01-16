package aaagt.cloudservice.jwt.service.impl;

import aaagt.cloudservice.jwt.config.JwtProperties;
import aaagt.cloudservice.jwt.dto.JwtPayloadDto;
import com.auth0.jwt.JWT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {aaagt.cloudservice.app.App.class})
class JwtServiceImplTest {

    private static final Logger log = Logger.getLogger(JwtServiceImplTest.class.getName());

    @Autowired
    JwtServiceImpl jwtService;

    @Autowired
    JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void generateToken() {
        long milliseconds = 999999000;
        Instant issuedAt = Instant.ofEpochMilli(milliseconds);
        Instant expireAt = Instant.ofEpochMilli(milliseconds).plus(this.jwtProperties.tokenExpiry());
        var payload = new JwtPayloadDto("test subject", issuedAt, expireAt);
        var token = jwtService.generateToken(payload);
        var expected = "test subject";
        log.info("Token: " + token);
        assertEquals(expected, JWT.decode(token).getSubject());
    }

    /*@Test
    void getSubjectFromJWT() {
    }

    @Test
    void validateToken() {
    }*/

}
