package com.nksbookstore.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nksbookstore.user.model.UserDTO;
import com.nksbookstore.user.entity.User;
import com.nksbookstore.user.exception.UserAlreadyExistsException;
import com.nksbookstore.user.repository.UserRepository;
import com.nksbookstore.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public List<UserDTO> getAllUsers() {
        
        List<User> usersList = new ArrayList<>();
        usersList = userRepository.findAll();

        List<UserDTO> resultList = new ArrayList<>();
        for(User user : usersList) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(user.getPassword());
            resultList.add(userDTO);
        }
        System.out.println(resultList);
        return resultList;

    }

    public Boolean register(UserDTO userDTO) {

        if(userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        };

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());

        userRepository.save(user);

        return true;
    
    }

}
