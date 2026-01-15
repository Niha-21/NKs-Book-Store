package com.nksbookstore.order.service;

import com.nksbookstore.order.client.CartClient;
import com.nksbookstore.order.common.event.ClearCartEvent;
import com.nksbookstore.order.exception.CartEmptyException;
import com.nksbookstore.order.exception.CartServiceUnavailableException;
import com.nksbookstore.order.exception.UnauthorizedException;
import com.nksbookstore.order.kafka.ClearCartProducer;
import com.nksbookstore.order.model.CartResponseDTO;

import feign.FeignException;
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
    private final ClearCartProducer clearCartProducer;

    private static final String CART_CB = "cartService";

    @CircuitBreaker(name = CART_CB, fallbackMethod = "getCartFallback")
    public CartResponseDTO getCart() {
        return cartClient.getCart();
    }

    @CircuitBreaker(name = CART_CB, fallbackMethod = "clearCartFallback")
    public void clearCart(Long orderId, Long userId) {
        cartClient.clearCart();
    }

    public CartResponseDTO getCartFallback(Throwable ex) {
        log.error("Cart service unavailable, fallback triggered", ex);

        if(ex instanceof FeignException.NotFound e) {

            log.error("Cart Empty. status={}, message={}",
            e.status(), e.getMessage());
            throw new CartEmptyException("Cart Empty");

        } else if(ex instanceof FeignException e) {

            log.error("Cart service call failed. status={}, message={}",
            e.status(), e.getMessage());
            if (e.status() == 401 || e.status() == 403) {
                throw new UnauthorizedException("Unauthorized to access cart-service");           
            }
            throw new CartServiceUnavailableException("Cart service unavailable");

        } else {

            log.error("Cart Service Unavailable | Failed to get cart", ex);
            throw new CartServiceUnavailableException("Cart service unavailable");
        
        }
        
        // return new CartResponseDTO(List.of(), BigDecimal.ZERO);
    }

    public void clearCartFallback(Long orderId, Long userId, Throwable ex) {
        log.error("Failed to clear cart, will retry asynchronously", ex);
        // retry logic
        clearCartProducer.publish(
            new ClearCartEvent(orderId, userId)
        );
    }

}
