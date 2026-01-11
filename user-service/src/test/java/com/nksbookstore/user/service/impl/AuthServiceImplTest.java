package com.nksbookstore.user.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import com.nksbookstore.user.repository.UserRepository;

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
    private Authentication authentication;

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void login_shouldAuthenticateAndReturnJwtToken() {

        String username = "user";
        String password = "pass";
        String token = "jwt-token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtTokenProvider.generateToken(authentication))
                .thenReturn(token);

        String result = authService.login(username, password);

        assertEquals(token, result);
        assertEquals(authentication,
                SecurityContextHolder.getContext().getAuthentication());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(authentication);
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