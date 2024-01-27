package aaagt.cloudservice.jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
@ConfigurationProperties(prefix = "aaagt.cloudservice.jwt")
public record JwtProperties(
        String tokenIssuer,
        String tokenSecret,
        @DurationUnit(ChronoUnit.SECONDS) Duration tokenExpiry
) {

    @ConstructorBinding
    public JwtProperties {}

}
