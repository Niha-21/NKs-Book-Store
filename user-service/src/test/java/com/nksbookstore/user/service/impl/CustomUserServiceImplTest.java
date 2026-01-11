package com.nksbookstore.user.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.nksbookstore.user.entity.User;
import com.nksbookstore.user.model.CustomUserDetails;
import com.nksbookstore.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CustomUserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserServiceImpl customUserService;

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setUsername("niha");
        user.setPassword("encoded-password");

        when(userRepository.findByUsername("niha"))
                .thenReturn(Optional.of(user));

        CustomUserDetails userDetails =
                customUserService.loadUserByUsername("niha");

        assertNotNull(userDetails);
        assertEquals("niha", userDetails.getUsername());
        assertEquals("encoded-password", userDetails.getPassword());
        verify(userRepository).findByUsername("niha");
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> customUserService.loadUserByUsername("unknown"));

        verify(userRepository).findByUsername("unknown");
    }
}
