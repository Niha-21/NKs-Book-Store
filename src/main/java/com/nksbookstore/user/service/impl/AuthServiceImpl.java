package com.nksbookstore.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nksbookstore.user.model.UserDTO;
import com.nksbookstore.user.entity.User;
import com.nksbookstore.user.repository.UserRepository;
import com.nksbookstore.user.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
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
            System.out.println(userDTO);
            resultList.add(userDTO);
        }
        System.out.println(resultList);
        return resultList;

    }

    @Override
    public String register(UserDTO userDTO) {

        try{

            if(userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
                throw new RuntimeException("Username already exists");
            };

            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setEmail(userDTO.getEmail());

            userRepository.save(user);

        } catch(Exception e) {
            return e.getMessage();
        }

        return "User Registered successfully !";

    }
    
}
