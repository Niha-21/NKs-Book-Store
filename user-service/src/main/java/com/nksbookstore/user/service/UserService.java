package com.nksbookstore.user.service;

import java.util.List;

import com.nksbookstore.user.exception.UserAlreadyExistsException;
import com.nksbookstore.user.model.UserDTO;

public interface UserService {

    public List<UserDTO> getAllUsers();

    public Boolean register(UserDTO user) throws UserAlreadyExistsException;
    
}
