package com.nksbookstore.cart.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nksbookstore.cart.entity.Cart;
import com.nksbookstore.cart.entity.CartItem;
import com.nksbookstore.cart.exception.CartItemNotFoundException;
import com.nksbookstore.cart.exception.CartNotFoundException;
import com.nksbookstore.cart.model.CartItemDTO;
import com.nksbookstore.cart.repository.CartItemRepository;
import com.nksbookstore.cart.repository.CartRepository;
import com.nksbookstore.cart.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
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

        // if book already exists in cart, increase quantity
        for (CartItem item : cart.getCartItems()) {
            if (item.getBookId().equals(bookId)) {
                item.setQuantity(item.getQuantity() + quantity);
                cartItemRepository.save(item);
                return;
            }
        }

        // create new CartItem
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setBookId(bookId);
        cartItem.setQuantity(quantity);

        cart.getCartItems().add(cartItem);

    }

    @Override
    public List<CartItemDTO> getCartItems() {

        Long userId = Long.parseLong(getLoggedInUserId());

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        return cart.getCartItems()
                    .stream()
                    .map(this::convertEntityToDTO)
                    .toList();
    }

    @Override
    @Transactional
    public void removeCartItem(Long cartItemId) {

        Long userId = Long.parseLong(getLoggedInUserId());

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
                                        .filter(item -> item.getId().equals(cartItemId))
                                        .findFirst();
        
        if(cartItemOptional.isPresent()) {
            
            CartItem itemToRemove = cartItemOptional.get();
            cart.getCartItems().remove(itemToRemove);

        } else {
            throw new CartItemNotFoundException("CartItem not found");
        }
    
    }

    @Override
    @Transactional
    public void clearCart() {

        Long userId = Long.parseLong(getLoggedInUserId());

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user"));

        cart.getCartItems().clear();

    }

    private CartItemDTO convertEntityToDTO(CartItem cartItem) {

        return new CartItemDTO(
            cartItem.getId(),
            cartItem.getCart().getId(),
            cartItem.getBookId(),
            cartItem.getQuantity()
        );

    }

    private String getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getPrincipal().toString();
    }
    
}
