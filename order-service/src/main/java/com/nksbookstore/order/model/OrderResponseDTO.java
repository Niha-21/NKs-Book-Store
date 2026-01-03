package com.nksbookstore.order.model;

import java.math.BigDecimal;
import java.util.List;

import com.nksbookstore.order.entity.OrderStatus;

import lombok.Data;

@Data
public class OrderResponseDTO {

    private Long orderId;
    private Long userId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private List<OrderItemResponseDTO> items;

}
