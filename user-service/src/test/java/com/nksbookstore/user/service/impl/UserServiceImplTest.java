package com.nksbookstore.user.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nksbookstore.user.entity.User;
import com.nksbookstore.user.exception.UserAlreadyExistsException;
import com.nksbookstore.user.model.UserDTO;
import com.nksbookstore.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllUsers_shouldReturnUserDTOList() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("pass1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("pass2");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2@example.com", result.get(1).getEmail());
    }

    @Test
    void register_shouldSaveUserWhenNotExists() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newuser");
        userDTO.setPassword("plaintext");
        userDTO.setEmail("newuser@example.com");

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plaintext")).thenReturn("encodedPass");

        Boolean result = userService.register(userDTO);

        assertTrue(result);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("newuser", savedUser.getUsername());
        assertEquals("encodedPass", savedUser.getPassword());
        assertEquals("newuser@example.com", savedUser.getEmail());
    }

    @Test
    void register_shouldThrowExceptionWhenUserExists() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("existinguser");

        when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(userDTO));
        verify(userRepository, never()).save(any());
    }
}
