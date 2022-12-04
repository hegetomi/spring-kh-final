package com.hegetomi.userservice.service;

import com.hegetomi.userservice.model.AuthUser;
import com.hegetomi.userservice.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<AuthUser> register(String email, String username, String password) {
        //if we login with username, then it must be unique
        return authUserRepository.findByUsername(username).isPresent()
                ? Optional.empty()
                : Optional.of(authUserRepository.save(AuthUser.builder()
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(Set.of("customer"))
                .build()));
    }

}
