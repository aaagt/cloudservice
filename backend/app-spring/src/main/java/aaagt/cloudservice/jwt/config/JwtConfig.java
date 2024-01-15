package aaagt.cloudservice.jwt.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @NestedConfigurationProperty
    public JwtProperties properties;

    public JwtConfig(final JwtProperties properties) {
        this.properties = properties;
    }

    /*@Bean
    public JwtProperties item() {
        return new JwtProperties();
    }*/


    @Bean
    public JWTVerifier jwtVerifier(final Algorithm algorithm) {
        return JWT.require(algorithm)
                .acceptLeeway(10)
                .withIssuer(this.properties.getTokenIssuer())
                .build();
    }

    @Bean
    public Algorithm jwtAlgorithm() {
        return Algorithm.HMAC256(this.properties.getTokenSecret());
    }

    @Bean
    public JWT jwt() {
        return new JWT();
    }
}
