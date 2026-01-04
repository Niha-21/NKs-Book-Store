package com.nksbookstore.order.client;

import com.nksbookstore.order.model.CartItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "cart-service", url = "${cart.service.url}")
public interface CartClient {

    @GetMapping("/cart/items")
    List<CartItemDTO> getCartItems();
}
