package com.hegetomi.userservice.service.oauth2;


import com.hegetomi.userservice.model.AuthUser;
import com.hegetomi.userservice.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FacebookLoginService {

    private final AuthUserRepository authUserRepository;
    private static final String FB_API_PATH = "https://graph.facebook.com/v3.0/me?fields=id,name,email,picture&access_token=";

    public UserDetails getUserDetailsForToken(String fbToken) {

        FacebookUserInfo user = getFbFromToken(fbToken);
        AuthUser authUser = loadOrCreate(user);
        return authUser;
    }

    private AuthUser loadOrCreate(FacebookUserInfo user) {
        Optional<AuthUser> userOptional = authUserRepository.findByFbId(String.valueOf(user.getId()));
        if (userOptional.isEmpty()) {
            return authUserRepository.save(AuthUser.builder()
                    .fbId(user.getId())
                    .username(user.getName())
                    .password("asd")
                    .email(user.getEmail())
                    .roles(Set.of("customer"))
                    .build());
        }
        return userOptional.get();
    }

    private FacebookUserInfo getFbFromToken(String fbToken) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<FacebookUserInfo> user = restTemplate.getForEntity(FB_API_PATH + fbToken, FacebookUserInfo.class);
        return user.getBody();
    }
}
