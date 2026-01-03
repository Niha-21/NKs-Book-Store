package com.nksbookstore.order.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemResponseDTO {

    private Long bookId;
    private String bookName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}
