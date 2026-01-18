package com.nksbookstore.user.service.impl;

import java.time.Duration;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nksbookstore.user.config.JwtTokenProvider;
import com.nksbookstore.user.entity.User;
import com.nksbookstore.user.model.AuthResponse;
import com.nksbookstore.user.repository.UserRepository;
import com.nksbookstore.user.service.AuthService;
import com.nksbookstore.user.service.RefreshTokenStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenStore refreshTokenStore;

    private static final Duration REFRESH_TTL = Duration.ofDays(7);

    public AuthResponse login(String username, String password) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
                
        String accessToken = jwtTokenProvider.generateToken(authentication);

        String refreshToken = UUID.randomUUID().toString();

        refreshTokenStore.save(refreshToken, user.getId(), REFRESH_TTL);
        
        return new AuthResponse(accessToken, refreshToken);

    }

    @Override
    public AuthResponse refresh(String refreshToken) {

        Long userId = refreshTokenStore.getUserId(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        String newAccessToken = jwtTokenProvider.generateToken(userId);

        return new AuthResponse(newAccessToken, refreshToken);
    }

    public Boolean checkUserExistence(String id) {

        Long userId = Long.parseLong(id);
        return userRepository.existsById(userId);
    
    }

    @Override
    public void logout(String refreshToken) {
        refreshTokenStore.delete(refreshToken);
    }

}
