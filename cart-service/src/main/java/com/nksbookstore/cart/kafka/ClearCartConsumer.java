package com.nksbookstore.cart.kafka;

import com.nksbookstore.cart.common.event.ClearCartEvent;
import com.nksbookstore.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClearCartConsumer {

    private final CartService cartService;
    
    @RetryableTopic(
        attempts = "3",                 
        backoff = @Backoff(
            delay = 2000,            
            multiplier = 2.0        
        ),
        dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "clear-cart")
    public void consume(ClearCartEvent event, Acknowledgment ack) {

        log.info("Received clear cart event for userId={}", event.getUserId());
        
        cartService.clearCartForUser(event.getUserId()); 
        ack.acknowledge();

    }
}
