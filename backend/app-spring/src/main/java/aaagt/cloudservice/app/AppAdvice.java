package aaagt.cloudservice.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AppAdvice {

    /*@ExceptionHandler(RuntimeException.class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException exception) {
        log.error("Error:", exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto("Internal server error", 0));
    }*/

    /*@ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(RuntimeException exception) {
        log.error("Error:", exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto("Bad Credentials", 2));
    }*/

}
