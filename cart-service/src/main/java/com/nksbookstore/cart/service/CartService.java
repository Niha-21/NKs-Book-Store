package com.nksbookstore.cart.service;

import java.util.List;

import com.nksbookstore.cart.model.CartItemDTO;

public interface CartService {
        
    void addToCart(CartItemDTO cartItem);

    List<CartItemDTO> getCartItems();

    void removeCartItem(Long cartItemId);

    void clearCart();

    public void updateCartItemQuantity(CartItemDTO cartItem);
    
}
