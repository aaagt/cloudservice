package aaagt.cloudservice.security.web;

import aaagt.cloudservice.security.web.dto.LoginRequestDto;
import aaagt.cloudservice.security.web.dto.LoginResponseDto;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        log.info("Login: {}", loginRequestDto);
        return new ResponseEntity<>(
                new LoginResponseDto(loginService.login(loginRequestDto)),
                HttpStatus.OK);
    }

}
