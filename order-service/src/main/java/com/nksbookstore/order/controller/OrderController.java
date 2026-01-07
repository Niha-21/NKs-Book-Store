package com.nksbookstore.order.controller;

import com.nksbookstore.order.model.OrderResponseDTO;
import com.nksbookstore.order.service.OrderService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder() {
        return new ResponseEntity<>(
                orderService.createOrder(),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUser() {
        return ResponseEntity.ok(
                orderService.getOrdersByUser()
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable @NotNull Long orderId) {
        return ResponseEntity.ok(
                orderService.getOrderById(orderId)
        );
    }
    
}
