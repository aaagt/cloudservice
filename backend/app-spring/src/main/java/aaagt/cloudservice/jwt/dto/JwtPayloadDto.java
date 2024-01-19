package aaagt.cloudservice.jwt.dto;

import java.time.Instant;
import java.util.Optional;

public record JwtPayloadDto(
        String subject,
        Optional<Instant> issuedAt,
        Optional<Instant> expireAt
) {}
