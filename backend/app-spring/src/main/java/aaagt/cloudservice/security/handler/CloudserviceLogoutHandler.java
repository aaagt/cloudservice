package aaagt.cloudservice.security.handler;

import aaagt.cloudservice.user.repository.UserTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CloudserviceLogoutHandler implements LogoutHandler {

    private final UserTokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("auth-token");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("Auth header '{}' is not valid", authHeader);
            return;
        }

        var jwt = authHeader.substring(7);

        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if (storedToken == null) {
            log.debug("JWT token '{}' is not found in repository", jwt);
            return;
        }

        storedToken.setRevoked(true);
        tokenRepository.save(storedToken);
        SecurityContextHolder.clearContext();
    }
}
