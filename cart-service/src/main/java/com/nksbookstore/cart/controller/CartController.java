package com.nksbookstore.cart.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nksbookstore.cart.model.CartItemDTO;
import com.nksbookstore.cart.service.CartService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity addToCart(@RequestBody CartItemDTO cartItem) {
        
        cartService.addToCart(cartItem);
        
        return ResponseEntity.ok(null);
    }

    @GetMapping("/get")
    public List<CartItemDTO> getCartItems() {
        
        return cartService.getCartItems();

    }
    
    

}
