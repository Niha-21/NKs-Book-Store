package com.nksbookstore.cart.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDTO {

    private Long id;

    private String title;

    private String imageUrl;
    
    private BigDecimal price;
    
}
