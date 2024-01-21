package aaagt.cloudservice.jwt.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    public final JwtProperties properties;

    @Bean
    public JWTVerifier jwtVerifier(final Algorithm algorithm) {
        return JWT.require(algorithm)
                .acceptLeeway(10)
                .withIssuer(this.properties.tokenIssuer())
                .build();
    }

    @Bean
    public Algorithm jwtAlgorithm() {
        return Algorithm.HMAC256(this.properties.tokenSecret());
    }

}
