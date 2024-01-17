package aaagt.cloudservice.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/logout")
public class LogoutController {

    @PostMapping
    public ResponseEntity<?> logout() {
        log.info("Logout");
        String token = "generated token";
        return ResponseEntity.ok().build();
    }

}
