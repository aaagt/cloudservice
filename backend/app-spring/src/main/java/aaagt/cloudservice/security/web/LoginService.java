package aaagt.cloudservice.security.web;

import aaagt.cloudservice.jwt.dto.JwtPayloadDto;
import aaagt.cloudservice.jwt.service.JwtService;
import aaagt.cloudservice.security.web.dto.LoginRequestDto;
import aaagt.cloudservice.user.entity.User;
import aaagt.cloudservice.user.entity.UserToken;
import aaagt.cloudservice.user.repository.UserRepository;
import aaagt.cloudservice.user.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserTokenRepository userTokenRepository;
    private final UserRepository userRepository;

    public String login(LoginRequestDto loginRequestDto) {
        var authToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getLogin(),
                loginRequestDto.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var user = userRepository.findByUsername(loginRequestDto.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("Not found on login"));
        revokeAllUserTokens(user);

        var payload = new JwtPayloadDto(
                authentication.getName(),
                Optional.empty(),
                Optional.empty()
        );
        var jwt = jwtService.generateToken(payload);
        saveUserToken(user, jwt);

        return jwt;
    }


    private void saveUserToken(User user, String jwtToken) {
        var token = UserToken.builder()
                .user(user)
                .token(jwtToken)
                .revoked(false)
                .build();
        userTokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = userTokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> token.setRevoked(true));
        userTokenRepository.saveAll(validUserTokens);
    }

}
