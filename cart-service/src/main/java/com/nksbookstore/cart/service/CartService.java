package com.nksbookstore.cart.service;

import com.nksbookstore.cart.model.CartItemDTO;
import com.nksbookstore.cart.model.CartResponseDTO;

public interface CartService {
        
    void addToCart(CartItemDTO cartItem);

    CartResponseDTO getCart();

    void removeCartItem(Long cartItemId);

    void clearCart();

    public void updateCartItemQuantity(CartItemDTO cartItem);

    void clearCartForUser(Long userId);
    
}
