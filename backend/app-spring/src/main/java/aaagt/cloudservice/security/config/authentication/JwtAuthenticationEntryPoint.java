package aaagt.cloudservice.security.config.authentication;

import aaagt.cloudservice.app.dto.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        authException.printStackTrace();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        var mapper = new ObjectMapper();
        var body = mapper.writeValueAsString(new ErrorResponseDto("Bad credentials", 1));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(body);
    }

}
