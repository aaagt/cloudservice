package aaagt.cloudservice.jwt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Data
@ConfigurationProperties(prefix = "aaagt.cloudservice.jwt")
public class JwtProperties {
    String tokenIssuer;
    String tokenSecret;
    @DurationUnit(ChronoUnit.SECONDS)
    Duration tokenExpiry;
}
