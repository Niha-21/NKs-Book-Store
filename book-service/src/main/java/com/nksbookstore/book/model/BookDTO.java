package com.nksbookstore.book.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private Long id;

    private String title;

    private String description;
    
    private String coverImg;

    private String imageUrl;

    private BigDecimal price;

}
