package com.nksbookstore.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nksbookstore.user.model.UserDTO;
import com.nksbookstore.user.config.JwtTokenProvider;
import com.nksbookstore.user.entity.User;
import com.nksbookstore.user.repository.UserRepository;
import com.nksbookstore.user.service.UserDetailsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

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

        try{

            if(userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
                return false;
            };

            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setEmail(userDTO.getEmail());

            userRepository.save(user);

        } catch(Exception e) {
            e.printStackTrace();
        }

        return true;

    }

}
