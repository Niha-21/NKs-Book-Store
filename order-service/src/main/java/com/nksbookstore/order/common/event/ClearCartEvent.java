package com.nksbookstore.order.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClearCartEvent {
    
    private Long orderId;
    private Long userId;
    
}
