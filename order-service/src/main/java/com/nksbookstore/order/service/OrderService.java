package com.nksbookstore.order.service;

import com.nksbookstore.order.model.OrderResponseDTO;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrder();

    List<OrderResponseDTO> getOrdersByUser();

    OrderResponseDTO getOrderById(Long orderId);

}
