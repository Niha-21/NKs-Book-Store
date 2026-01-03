package com.nksbookstore.order.service.impl;

import com.nksbookstore.order.model.OrderItemResponseDTO;
import com.nksbookstore.order.model.OrderResponseDTO;
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

    @Override
    public OrderResponseDTO createOrder() {

        // TODO: fetch cart items from Cart Service
        // For now assume cartItems available

        Long userId = Long.parseLong(getLoggedInUserId());

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CREATED);

        // TODO: map cart items → order items
        // calculate totalAmount

        order.setTotalAmount(BigDecimal.ZERO); // placeholder

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

    // ---------------- MAPPING ----------------

    private OrderResponseDTO mapToOrderResponse(Order order) {

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());

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
