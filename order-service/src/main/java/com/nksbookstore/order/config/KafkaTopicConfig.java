package com.nksbookstore.order.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic clearCartTopic() {
        return new NewTopic("clear-cart", 3, (short) 1);
    }

    @Bean
    public NewTopic clearCartDLT() {
        return new NewTopic("clear-cart.DLT", 3, (short) 1);
    }
}
