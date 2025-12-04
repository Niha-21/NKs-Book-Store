package com.nksbookstore.user.service;

import java.util.List;

import com.nksbookstore.user.model.UserDTO;

public interface UserDetailsService {

    public List<UserDTO> getAllUsers();

    public Boolean register(UserDTO user);
    
}
