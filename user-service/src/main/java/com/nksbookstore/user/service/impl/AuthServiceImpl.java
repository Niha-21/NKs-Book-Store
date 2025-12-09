package com.nksbookstore.user.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nksbookstore.user.config.JwtTokenProvider;
import com.nksbookstore.user.repository.UserRepository;
import com.nksbookstore.user.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public String login(String username, String password) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        return jwtTokenProvider.generateToken(authentication);

    }

    public Boolean checkUserExistence(String id) {

        Long userId = Long.parseLong(id);
        return userRepository.existsById(userId);
    
    }

}
