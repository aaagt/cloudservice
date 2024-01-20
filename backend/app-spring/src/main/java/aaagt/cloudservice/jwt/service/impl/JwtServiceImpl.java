package aaagt.cloudservice.jwt.service.impl;

import aaagt.cloudservice.jwt.config.JwtProperties;
import aaagt.cloudservice.jwt.dto.JwtPayloadDto;
import aaagt.cloudservice.jwt.service.JwtService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Log4j2
@Service
public class JwtServiceImpl implements JwtService {

    private final JWTVerifier verifier;

    private final Algorithm jwtAlgorithm;

    private final JwtProperties properties;

    public JwtServiceImpl(JWTVerifier verifier,
                          Algorithm jwtAlgorithm,
                          JwtProperties properties) {
        this.verifier = verifier;
        this.jwtAlgorithm = jwtAlgorithm;
        this.properties = properties;
    }

    @Override
    public String generateToken(JwtPayloadDto payload) {
        Instant issuedAt = payload.issuedAt()
                .orElse(Instant.now());

        Instant expireAt = payload.expireAt()
                .orElse(Instant.now().plus(this.properties.tokenExpiry()));

        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withSubject(payload.subject())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expireAt)
                .withIssuer(properties.tokenIssuer())
                .sign(jwtAlgorithm);
    }

    @Override
    public String getSubjectFromJWT(String token) {
        return JWT.decode(token).getSubject();
    }

    @Override
    public boolean isTokenNotValid(String jwtToken) throws JWTVerificationException {
        try {
            verifier.verify(jwtToken);
        } catch (JWTVerificationException e) {
            log.info("JWTVerificationException", e);
            return true;
        }
        return false;
    }

}
