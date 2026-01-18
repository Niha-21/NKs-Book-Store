package com.nksbookstore.user.service;

import com.nksbookstore.user.model.AuthResponse;

public interface AuthService {
    
    public AuthResponse login(String username, String password);
    
    public Boolean checkUserExistence(String id);

    public AuthResponse refresh(String refreshToken);

    public void logout(String refreshToken);

} 