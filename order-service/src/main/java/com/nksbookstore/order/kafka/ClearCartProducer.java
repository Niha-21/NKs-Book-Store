package com.nksbookstore.order.kafka;

import com.nksbookstore.order.common.event.ClearCartEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClearCartProducer {

    private final KafkaTemplate<String, ClearCartEvent> kafkaTemplate;

    public void publish(ClearCartEvent event) {
        kafkaTemplate.send("clear-cart", event);
    }
}
