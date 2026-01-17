package com.nksbookstore.cart.kafka;

import com.nksbookstore.cart.common.event.ClearCartEvent;
import com.nksbookstore.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    private static final long BACKOFF_DELAY = 2000;
    private static final double BACKOFF_MULTIPLIER = 2.0;

    @RetryableTopic(
        attempts = "${spring.kafka.consumer.clear-cart.retry.attempts}",
        backoff = @Backoff(
            delay = BACKOFF_DELAY,
            multiplier = BACKOFF_MULTIPLIER
        ),
        dltTopicSuffix = "${spring.kafka.topics.clear-cart.dlt-suffix}"
    )
    @KafkaListener(topics = "${spring.kafka.topics.clear-cart.name}")
    public void consume(ClearCartEvent event, Acknowledgment ack) {

        log.info("Received clear cart event for userId={}", event.getUserId());

        cartService.clearCartForUser(event.getUserId());
        ack.acknowledge();
        
        log.info("Event for userId={} orderId={} acknowledged.", event.getUserId(), event.getOrderId());

    }

    @KafkaListener(topics = "${spring.kafka.topics.clear-cart-dlt.name}")
    public void handleRetryDlt(ClearCartEvent event, Exception ex) {
        log.error("Retries exhausted for event {}", event, ex);
    }

}
