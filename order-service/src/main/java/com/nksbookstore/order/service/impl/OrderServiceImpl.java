package com.nksbookstore.order.service.impl;

import com.nksbookstore.order.model.CartItemDTO;
import com.nksbookstore.order.model.CartResponseDTO;
import com.nksbookstore.order.model.OrderItemResponseDTO;
import com.nksbookstore.order.model.OrderResponseDTO;
import com.nksbookstore.order.client.CartClient;
import com.nksbookstore.order.entity.Order;
import com.nksbookstore.order.entity.OrderItem;
import com.nksbookstore.order.entity.OrderStatus;
import com.nksbookstore.order.repository.OrderRepository;
import com.nksbookstore.order.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartClient cartClient;

    @Override
    @Transactional
    public OrderResponseDTO createOrder() {
        
        CartResponseDTO cartResponseDTO = null;
        cartResponseDTO = cartClient.getCart();

        if (cartResponseDTO == null) {
            throw new IllegalStateException("Cart is empty, cannot create order");
        }

        List<CartItemDTO> cartItems = cartResponseDTO.getCartItems();

        // BigDecimal totalAmount = cartItems.stream()
        //     .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
        //     .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long userId = Long.parseLong(getLoggedInUserId());

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
                orderItem.setQuantity(item.getQuantity());
                orderItem.setPrice(item.getPrice());
                return orderItem;
            })
            .toList();
        
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);
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
                .orElseThrow(() -> new RuntimeException("Order not found"));
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
        dto.setSubTotal(
                item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
        return dto;
    }

    private String getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getPrincipal().toString();
    }

}
