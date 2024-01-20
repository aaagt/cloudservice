package aaagt.cloudservice.security.config.authentication;

import aaagt.cloudservice.jwt.service.JwtService;
import aaagt.cloudservice.user.entity.UserToken;
import aaagt.cloudservice.user.repository.UserTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserTokenRepository userTokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        var jwt = getJWTFromRequest(request);
        log.debug("Token from request: {}", jwt);

        if (isTokenNotValid(jwt)) {
            log.debug("Token is not valid: {}", jwt);
            filterChain.doFilter(request, response);
            return;
        }

        var username = jwtService.getSubjectFromJWT(jwt);
        log.debug("Token granted to: {}", username);

        var authenticationToken = buildAuthenticationToken(username, request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken buildAuthenticationToken(
            String username,
            HttpServletRequest request
    ) {
        var userDetails = userDetailsService.loadUserByUsername(username);
        var authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        var webAuthenticationDetails = new WebAuthenticationDetailsSource()
                .buildDetails(request);
        authToken.setDetails(webAuthenticationDetails);
        return authToken;
    }

    private boolean isTokenNotValid(String jwt) {
        return (!StringUtils.hasText(jwt)) ||
                isTokenRevoked(jwt) ||
                jwtService.isTokenNotValid(jwt);
    }

    private boolean isTokenRevoked(String jwt) {
        return userTokenRepository.findByToken(jwt)
                .map(UserToken::isRevoked)
                .orElse(true);
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("auth-token");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
