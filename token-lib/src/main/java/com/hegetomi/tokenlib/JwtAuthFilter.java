package com.hegetomi.tokenlib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {


    public static final String BEARER_ = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";

    @Autowired
    JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION);
        Authentication authentication = userDetails(authHeader, jwtService);
        if (null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    public static Authentication userDetails(String authHeader, JwtService jwtService) {
        if (authHeader != null && authHeader.startsWith(BEARER_)) {
            String jwtToken = authHeader.substring(BEARER_.length());
            UserDetails principal = jwtService.parseJwt(jwtToken);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            return authentication;
        }
        return null;
    }


}