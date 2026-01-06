package com.nksbookstore.order.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartResponseDTO {

    private List<CartItemDTO> cartItems;
    
    private BigDecimal cartTotal;

}