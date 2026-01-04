package com.nksbookstore.cart.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemDTO {

    private Long id;

    private Long cartId;

    private Long bookId;
    
    private String bookName;
    
    private BigDecimal price;

    private Integer quantity;

}
