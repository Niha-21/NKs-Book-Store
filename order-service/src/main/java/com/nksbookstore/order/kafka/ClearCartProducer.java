package com.nksbookstore.order.kafka;

import com.nksbookstore.order.common.event.ClearCartEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClearCartProducer {

    private final KafkaTemplate<String, ClearCartEvent> kafkaTemplate;

    @Value("${spring.kafka.topics.clear-cart.name}")
    private String clearCartTopic;

    public void publish(ClearCartEvent event) {
        kafkaTemplate.send(clearCartTopic, event);
        log.info("Event published to topic={} for orderId={}", clearCartTopic, event.getOrderId());
    }

}
