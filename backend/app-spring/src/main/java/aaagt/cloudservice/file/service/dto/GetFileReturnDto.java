package aaagt.cloudservice.file.service.dto;

import org.springframework.core.io.Resource;

public record GetFileReturnDto(
        Resource file,
        String hash
) {}
