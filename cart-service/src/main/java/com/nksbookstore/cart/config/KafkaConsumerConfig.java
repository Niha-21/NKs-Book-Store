package com.nksbookstore.cart.config;

import com.nksbookstore.cart.exception.CartNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public DefaultErrorHandler errorHandler() {

        // RetryableTopic handles retries
        FixedBackOff backOff = new FixedBackOff(0L, 0);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
            (record, ex) -> {
                log.error(
                    "Non-retryable exception. Skipping record. topic={}, partition={}, offset={}, key={}",
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    record.key(),
                    ex
                );
            },
            backOff
        );

        // Business rule exceptions (no retry)
        errorHandler.addNotRetryableExceptions(
            CartNotFoundException.class
        );

        // Technical exceptions (no retry)
        errorHandler.addNotRetryableExceptions(
            DeserializationException.class,
            SerializationException.class,
            IllegalArgumentException.class,
            ClassCastException.class
        );

        return errorHandler;
    }
}
