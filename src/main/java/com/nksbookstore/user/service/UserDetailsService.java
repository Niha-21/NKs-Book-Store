package com.nksbookstore.user.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.nksbookstore.user.model.UserDTO;

public interface UserDetailsService {

    public List<UserDTO> getAllUsers();

    public String register(UserDTO user);
    
}
