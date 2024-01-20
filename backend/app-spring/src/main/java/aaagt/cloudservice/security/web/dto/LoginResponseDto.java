package aaagt.cloudservice.security.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {

    @JsonProperty("auth-token")
    private String authToken;

}
