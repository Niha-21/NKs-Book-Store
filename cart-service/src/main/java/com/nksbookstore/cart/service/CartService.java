package com.nksbookstore.cart.service;

import java.util.List;

import com.nksbookstore.cart.model.CartItemDTO;

public interface CartService {
        
    void addToCart(CartItemDTO cartItem);

    List<CartItemDTO> getCartItems();

    void removeItem(Long cartItemId);

    void clearCart(Long userId);
    
}
