package com.nksbookstore.user.service;

import java.util.List;
import com.nksbookstore.user.model.UserDTO;

public interface AuthService {

    public List<UserDTO> getAllUsers();

    public String register(UserDTO user);
    
} 