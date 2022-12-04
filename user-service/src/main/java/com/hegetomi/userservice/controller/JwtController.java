package com.hegetomi.userservice.controller;

import com.hegetomi.tokenlib.JwtService;
import com.hegetomi.userservice.dto.LoginDto;
import com.hegetomi.userservice.dto.RegistrationDto;
import com.hegetomi.userservice.model.AuthUser;
import com.hegetomi.userservice.service.AppUserDetailsService;
import com.hegetomi.userservice.service.AuthService;
import com.hegetomi.userservice.service.oauth2.FacebookLoginService;
import lombok.RequiredArgsConstructor;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class JwtController {


    private final AuthenticationManager authenticationManager;

    private final FacebookLoginService facebookLoginService;
    private final AuthService authService;

    private final JwtService jwtService;

    @PostMapping("/auth/login")
    public String login(@RequestBody LoginDto loginDto) {

        UserDetails userDetails = null;
        String fbToken = loginDto.getFbToken();
        if (fbToken != null) {
            userDetails = facebookLoginService.getUserDetailsForToken(fbToken);
        } else {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(), loginDto.getPassword()));
            userDetails = (UserDetails) auth.getPrincipal();
        }
        return jwtService.createJwtToken(userDetails);
    }

    @PostMapping("/auth/register")
    public ResponseEntity register(@RequestBody @Valid RegistrationDto registrationDto) {
        String email = registrationDto.getEmail();
        String username = registrationDto.getUsername();
        String password = registrationDto.getPassword();
        Optional<AuthUser> register = authService.register(email, username, password);
        if (register.isPresent()) {
            return ResponseEntity.status(201).build();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }


}
