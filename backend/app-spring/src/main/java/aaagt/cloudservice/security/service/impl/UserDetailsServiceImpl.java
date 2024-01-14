package aaagt.cloudservice.security.service.impl;

import aaagt.cloudservice.security.entity.User;
import aaagt.cloudservice.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        log.info("Find user: " + user.toString());
        return user
                .map(u -> {return new UserDetailsImpl(u);})
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

}
