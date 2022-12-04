package com.hegetomi.userservice.service;

import com.hegetomi.userservice.model.AuthUser;
import com.hegetomi.userservice.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class InitService {

    private final AuthUserRepository authUserRepository;

    public void deleteAll() {
        authUserRepository.deleteAll();
    }

    public void insertInit() {
        if (authUserRepository.findByUsername("admin").isEmpty()) {
            AuthUser user = AuthUser.builder().username("admin").password(new BCryptPasswordEncoder().encode("admin"))
                    .roles(Set.of("admin", "customer")).email("admin@admin.com").build();
            authUserRepository.save(user);
        }
    }
}
