package com.nksbookstore.order.client;

import com.nksbookstore.order.model.CartResponseDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cart-service")
public interface CartClient {

    @GetMapping("/cart")
    CartResponseDTO getCart();
}
