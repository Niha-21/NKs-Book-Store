package com.nksbookstore.cart.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nksbookstore.cart.entity.Cart;
import com.nksbookstore.cart.entity.CartItem;
import com.nksbookstore.cart.exception.BookNotFoundException;
import com.nksbookstore.cart.exception.BookServiceUnavailableException;
import com.nksbookstore.cart.exception.CartItemNotFoundException;
import com.nksbookstore.cart.exception.CartNotFoundException;
import com.nksbookstore.cart.exception.UnauthorizedException;
import com.nksbookstore.cart.model.CartItemDTO;
import com.nksbookstore.cart.model.CartResponseDTO;
import com.nksbookstore.cart.model.BookDTO;
import com.nksbookstore.cart.repository.CartItemRepository;
import com.nksbookstore.cart.repository.CartRepository;
import com.nksbookstore.cart.service.CartService;

import feign.FeignException;

import com.nksbookstore.cart.client.BookClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookClient bookClient;

    @Override
    @Transactional
    public void addToCart(CartItemDTO cartItemRequest) {

        // Get or create cart
        Long userId = Long.parseLong(getLoggedInUserId());
        Long bookId = cartItemRequest.getBookId();
        Integer quantity = cartItemRequest.getQuantity();

        Cart cart = getOrCreateCart(userId);

        Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
                                                .filter(item -> item.getBookId().equals(bookId))
                                                .findFirst();

        if(cartItemOptional.isPresent()) {
            cartItemOptional.get().setQuantity(quantity);
        } else {            
            // create new CartItem
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setBookId(bookId);
            cartItem.setQuantity(quantity);

            cart.getCartItems().add(cartItem);
        }    

    }

    private Cart getOrCreateCart(Long userId) {
        
        log.info("Fetching cart for userId={}", userId);
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    log.info("Creating new cart for userId={}", userId);
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });

    }

    @Override
    @Transactional
    public void updateCartItemQuantity(CartItemDTO cartItemRequest) {

        Long userId = Long.parseLong(getLoggedInUserId());
        Long cartItemId = cartItemRequest.getId();
        Integer quantity = cartItemRequest.getQuantity();

        Cart cart = getOrCreateCart(userId);

        Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
                                                .filter(item -> item.getId().equals(cartItemId))
                                                .findFirst();

        if(cartItemOptional.isPresent()) {
            if(quantity <= 0) {
                cart.getCartItems().remove(cartItemOptional.get());
                return;
            }
            cartItemOptional.get().setQuantity(quantity);
        } else {
            throw new CartItemNotFoundException("Item not found");
        }

    }

    @Override
    public CartResponseDTO getCart() {
        
        Long userId = Long.parseLong(getLoggedInUserId());
        log.info("Fetching cart for userId={}", userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        List<CartItemDTO> cartItems = 
                    cart.getCartItems()
                    .stream()
                    .map(cartItem -> {
                        try {
                            BookDTO book = bookClient.getBookById(cartItem.getBookId());
                            return convertEntityToDTO(cartItem, book);
                        } catch(FeignException.NotFound e) {
                            log.error("Book service call failed: status={}, message={}",
                            e.status(), e.getMessage());
                            throw new BookNotFoundException("Book Not Found");
                        } catch (FeignException.Unauthorized | FeignException.Forbidden e) {
                            log.error("Book service call failed: status={}, message={}",
                            e.status(), e.getMessage());
                            throw new UnauthorizedException("Unauthorized to access book-service");
                        } catch (FeignException e) {
                            log.error("Book service call failed: status={}, message={}",
                            e.status(), e.getMessage());
                            throw new BookServiceUnavailableException("Book service unavailable");
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();
        
        BigDecimal cartTotal = cartItems.stream()
            .map(item ->
            item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDTO(cartItems, cartTotal);
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
            throw new CartItemNotFoundException(" Cart Item not found");
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

    private CartItemDTO convertEntityToDTO(CartItem cartItem, BookDTO book) {

        return new CartItemDTO(
            cartItem.getId(),
            cartItem.getCart().getId(),
            book.getId(),
            book.getTitle(),
            book.getImageUrl(),
            book.getPrice(),
            cartItem.getQuantity()
        );

    }

    private String getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if(auth == null || auth.getPrincipal() == null) {               
            log.error("User not authenticated => {}", auth);
            throw new UnauthorizedException("User not authenticated");
        }

        return auth.getPrincipal().toString();
    }
    
}
