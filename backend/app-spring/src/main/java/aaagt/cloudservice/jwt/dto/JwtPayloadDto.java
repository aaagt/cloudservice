package aaagt.cloudservice.jwt.dto;

import java.time.Instant;

public record JwtPayloadDto(
        String subject,
        Instant issuedAt,
        Instant expireAt
) {}
