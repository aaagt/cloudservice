package aaagt.cloudservice.jwt.service;

import aaagt.cloudservice.jwt.dto.JwtPayloadDto;

public interface JwtService {
    String generateToken(JwtPayloadDto payload);

    String getSubjectFromJWT(String token);

    boolean validateToken(String token);
}
