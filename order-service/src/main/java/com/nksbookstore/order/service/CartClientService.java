package com.nksbookstore.order.service;

import com.nksbookstore.order.client.CartClient;
import com.nksbookstore.order.model.CartResponseDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartClientService {

    private final CartClient cartClient;

    private static final String CART_CB = "cartService";

    @CircuitBreaker(name = CART_CB, fallbackMethod = "getCartFallback")
    public CartResponseDTO getCart() {
        return cartClient.getCart();
    }

    @CircuitBreaker(name = CART_CB, fallbackMethod = "clearCartFallback")
    public void clearCart() {
        cartClient.clearCart();
    }

    public CartResponseDTO getCartFallback(Throwable ex) {
        log.error("Cart service unavailable, fallback triggered", ex);
        return new CartResponseDTO(List.of(), BigDecimal.ZERO);
    }

    public void clearCartFallback(Throwable ex) {
        log.error("Failed to clear cart, will retry asynchronously", ex);
        // to add retry logic later
    }
}
