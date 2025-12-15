package com.nksbookstore.cart.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nksbookstore.cart.model.CartItemDTO;
import com.nksbookstore.cart.service.CartService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<Void> addToCart(@RequestBody CartItemDTO cartItem) {
        
        cartService.addToCart(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItemDTO>> getCartItems() {
        
        return ResponseEntity.ok(cartService.getCartItems());

    }
    
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {

        cartService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {

        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/items")
    public ResponseEntity<Void> updateCartItemQuantity(@RequestBody CartItemDTO cartItem) {
        
        cartService.updateCartItemQuantity(cartItem);

        return ResponseEntity.noContent().build();
    }
    

}
