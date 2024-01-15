package aaagt.cloudservice.jwt.service.impl;

import aaagt.cloudservice.jwt.config.JwtProperties;
import aaagt.cloudservice.jwt.dto.JwtPayloadDto;
import aaagt.cloudservice.jwt.service.JwtService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Instant;
import java.util.UUID;

@Log4j2
public class JwtServiceImpl implements JwtService {

    private final JWTVerifier verifier;
    private final Algorithm jwtAlgorithm;

    @NestedConfigurationProperty
    public JwtProperties properties;

    public JwtServiceImpl(JWTVerifier verifier, Algorithm jwtAlgorithm) {
        this.verifier = verifier;
        this.jwtAlgorithm = jwtAlgorithm;
    }

    @Override
    public String generateToken(JwtPayloadDto payload) {
        Instant issuedAt = Instant.now();
        Instant expireAt = Instant.now().plus(this.properties.getTokenExpiry());
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withSubject(payload.subject())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expireAt)
                .sign(jwtAlgorithm);
    }

    @Override
    public String getSubjectFromJWT(String token) {
        return null;
    }

    @Override
    public boolean validateToken(String jwtToken) throws JWTVerificationException {
        try {
            verifier.verify(jwtToken);
        } catch (JWTVerificationException e) {
            log.info("JWTVerificationException", e);
            return false;
        }
        return true;
    }
}
