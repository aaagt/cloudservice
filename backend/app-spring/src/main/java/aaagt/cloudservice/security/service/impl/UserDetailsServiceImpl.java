package aaagt.cloudservice.security.service.impl;

import aaagt.cloudservice.security.dto.UserDetailsDto;
import aaagt.cloudservice.security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsDto loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new UserDetailsDto(user))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

}
