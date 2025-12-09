package com.nksbookstore.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nksbookstore.user.model.LoginRequest;
import com.nksbookstore.user.model.LoginResponse;
import com.nksbookstore.user.model.UserDTO;
import com.nksbookstore.user.service.AuthService;
import com.nksbookstore.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    private final AuthService authService;

    @GetMapping
    public List<UserDTO> getUsers() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        System.out.println("userId = " + principal.toString() );
        return userService.getAllUsers(); 
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDTO user) {

        userService.register(user); 
        
        return ResponseEntity.ok("User Registered");
        
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.username(), request.password());
        return ResponseEntity.ok(new LoginResponse(token));
    }

}
