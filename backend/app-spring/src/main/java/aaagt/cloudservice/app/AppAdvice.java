package aaagt.cloudservice.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AppAdvice {

    /*@ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> hande(RuntimeException exception) {
        log.error("Error:", exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto(exception.getMessage(), 0));
    }*/

}
