package com.nksbookstore.cart.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemDTO {

    private Long id;

    private Long cartId;

    private Long bookId;

    private Integer quantity;

}
