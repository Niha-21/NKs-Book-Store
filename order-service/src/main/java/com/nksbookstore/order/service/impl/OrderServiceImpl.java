package com.nksbookstore.order.service.impl;

import com.nksbookstore.order.model.CartItemDTO;
import com.nksbookstore.order.model.CartResponseDTO;
import com.nksbookstore.order.model.OrderItemResponseDTO;
import com.nksbookstore.order.model.OrderResponseDTO;
import com.nksbookstore.order.client.CartClient;
import com.nksbookstore.order.entity.Order;
import com.nksbookstore.order.entity.OrderItem;
import com.nksbookstore.order.entity.OrderStatus;
import com.nksbookstore.order.exception.CartEmptyException;
import com.nksbookstore.order.exception.CartServiceUnavailableException;
import com.nksbookstore.order.exception.OrderNotFoundException;
import com.nksbookstore.order.exception.UnauthorizedException;
import com.nksbookstore.order.repository.OrderRepository;
import com.nksbookstore.order.service.OrderService;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartClient cartClient;

    @Override
    @Transactional
    public OrderResponseDTO createOrder() {

        Long userId = Long.parseLong(getLoggedInUserId());
        log.info("Creating order for userId={}", userId);

        CartResponseDTO cartResponseDTO = null;

        try {
            cartResponseDTO =  cartClient.getCart();
        } catch(FeignException.NotFound e) {
            log.error("Cart service call failed: status={}, message={}",
              e.status(), e.getMessage());
            throw new CartEmptyException("Cart is empty");
        } catch (FeignException.Unauthorized | FeignException.Forbidden e) {
            log.error("Cart service call failed: status={}, message={}",
              e.status(), e.getMessage());
            throw new UnauthorizedException("Unauthorized to access cart");
        } catch (FeignException e) {
            log.error("Cart service call failed: status={}, message={}",
              e.status(), e.getMessage());
            throw new CartServiceUnavailableException("Cart service unavailable");
        }

        List<CartItemDTO> cartItems = cartResponseDTO.getCartItems();
        
        if (cartItems.isEmpty()) {
            log.error("Cart Empty for userId={}", userId);
            throw new CartEmptyException("No items in cart");
        }

        // BigDecimal totalAmount = cartItems.stream()
        //     .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
        //     .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(cartResponseDTO.getCartTotal());
        order.setStatus(OrderStatus.CREATED);

        List<OrderItem> orderItems = cartItems.stream()
            .map(item -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setBookId(item.getBookId());
                orderItem.setBookName(item.getBookName());
                orderItem.setImageUrl(item.getImageUrl());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setPrice(item.getPrice());
                return orderItem;
            })
            .toList();
        
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        try {
            //clearing cart
            cartClient.clearCart();
        } catch(FeignException e) {
            log.warn("Order {} created but failed to clear cart", savedOrder.getId(), e);
        }

        log.info("Created order for userId={}", userId);
        return mapToOrderResponse(savedOrder);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByUser() {
        
        Long userId = Long.parseLong(getLoggedInUserId());

        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

    @Override
    public OrderResponseDTO getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    
        return mapToOrderResponse(order);
    
    }
    
    private OrderResponseDTO mapToOrderResponse(Order order) {

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponseDTO> items = order.getOrderItems()
                .stream()
                .map(this::mapToOrderItemResponse)
                .toList();

        dto.setItems(items);
        return dto;
    }

    private OrderItemResponseDTO mapToOrderItemResponse(OrderItem item) {

        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setBookId(item.getBookId());
        dto.setBookName(item.getBookName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setImageUrl(item.getImageUrl());
        dto.setSubTotal(
                item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
        return dto;
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
