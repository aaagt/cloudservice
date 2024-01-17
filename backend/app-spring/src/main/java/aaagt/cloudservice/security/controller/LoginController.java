package aaagt.cloudservice.security.controller;

import aaagt.cloudservice.security.dto.LoginRequestDto;
import aaagt.cloudservice.security.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @PostMapping
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        log.info("Login: {}", loginRequestDto);
        String token = "generated token";
        return new ResponseEntity<>(new LoginResponseDto(token), HttpStatus.OK);
    }

}
