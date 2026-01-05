package com.nksbookstore.cart.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BookDTO {

    private Long id;

    private String title;

    private String imageUrl;
    
    private BigDecimal price;
    
}
