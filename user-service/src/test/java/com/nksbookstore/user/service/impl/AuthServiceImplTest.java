package com.nksbookstore.user.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nksbookstore.user.config.JwtTokenProvider;
import com.nksbookstore.user.entity.User;
import com.nksbookstore.user.repository.UserRepository;
import com.nksbookstore.user.service.RefreshTokenStore;
import com.nksbookstore.user.model.AuthResponse;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenStore refreshTokenStore;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void login_shouldAuthenticateAndReturnAuthResponse() {
        String username = "user";
        String password = "pass";
        String accessToken = "jwt-token";
        String refreshToken = "refresh-token";
        Long userId = 123L;
        User user = new User();
        user.setId(userId);

        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
                
        when(jwtTokenProvider.generateToken(authentication)).thenReturn(accessToken);
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));
                
        doNothing().when(refreshTokenStore).save(anyString(), eq(userId), any());

        AuthResponse response = authService.login(username, password);

        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
        assertNotNull(response.getRefreshToken());

        verify(refreshTokenStore).save(anyString(), eq(userId), any());

        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(authentication);
        verify(userRepository).findByUsername(username);
    }

    @Test
    void refresh_shouldReturnNewAccessToken() {
        String oldRefreshToken = "old-refresh-token";
        Long userId = 123L;
        String newAccessToken = "new-jwt-token";

        when(refreshTokenStore.getUserId(oldRefreshToken)).thenReturn(Optional.of(userId));
        when(jwtTokenProvider.generateToken(userId)).thenReturn(newAccessToken);

        AuthResponse response = authService.refresh(oldRefreshToken);

        assertNotNull(response);
        assertEquals(newAccessToken, response.getAccessToken());
        assertEquals(oldRefreshToken, response.getRefreshToken());

        verify(refreshTokenStore).getUserId(oldRefreshToken);
        verify(jwtTokenProvider).generateToken(userId);
    }

    @Test
    void refresh_shouldThrowWhenRefreshTokenInvalid() {
        String invalidToken = "invalid-token";

        when(refreshTokenStore.getUserId(invalidToken)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.refresh(invalidToken);
        });

        assertEquals("Invalid refresh token", exception.getMessage());
        verify(refreshTokenStore).getUserId(invalidToken);
        verifyNoMoreInteractions(jwtTokenProvider);
    }

    @Test
    void logout_shouldDeleteRefreshToken() {
        String refreshToken = "refresh-token-to-delete";

        doNothing().when(refreshTokenStore).delete(refreshToken);

        authService.logout(refreshToken);

        verify(refreshTokenStore).delete(refreshToken);
    }

    @Test
    void checkUserExistence_shouldReturnTrueIfUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        Boolean exists = authService.checkUserExistence("1");

        assertTrue(exists);
        verify(userRepository).existsById(1L);
    }

    @Test
    void checkUserExistence_shouldReturnFalseIfUserDoesNotExist() {
        when(userRepository.existsById(2L)).thenReturn(false);

        Boolean exists = authService.checkUserExistence("2");

        assertFalse(exists);
        verify(userRepository).existsById(2L);
    }
}
