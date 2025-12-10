package com.nksbookstore.cart.service.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nksbookstore.cart.entity.Cart;
import com.nksbookstore.cart.entity.CartItem;
import com.nksbookstore.cart.model.CartItemDTO;
import com.nksbookstore.cart.repository.CartItemRepository;
import com.nksbookstore.cart.repository.CartRepository;
import com.nksbookstore.cart.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public void addToCart(CartItemDTO cartItemRequest) {

        // Get or create cart
        Long userId = Long.parseLong(getLoggedInUserId());
        Long bookId = cartItemRequest.getBookId();
        Integer quantity = cartItemRequest.getQuantity();

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });

        // Check if book already exists in cart
        List<CartItem> existingItems = cartItemRepository.findByCartId(cart.getId());

        for (CartItem item : existingItems) {
            if (item.getBookId().equals(bookId)) {
                item.setQunatity(item.getQunatity() + quantity);
                cartItemRepository.save(item);
                return;
            }
        }

        // create new CartItem
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setBookId(bookId);
        cartItem.setQunatity(quantity);

        cartItemRepository.save(cartItem);
    }

    @Override
    public List<CartItemDTO> getCartItems() {

        Long userId = Long.parseLong(getLoggedInUserId());

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return cartItemRepository.findByCartId(cart.getId())
                                .stream().map(this::convertEntityToDTO).toList();
    }

    @Override
    public void removeItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        cartItemRepository.deleteAll(items);
    }

    private CartItemDTO convertEntityToDTO(CartItem cartItem) {

        return new CartItemDTO(
            cartItem.getId(),
            cartItem.getCart().getId(),
            cartItem.getBookId(),
            cartItem.getQunatity()
        );

    }

    private String getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getPrincipal().toString();
    }
    
}
