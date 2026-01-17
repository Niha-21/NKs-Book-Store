package com.nksbookstore.discovery_server.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topics.clear-cart.name}")
    private String clearCartTopic;

    @Value("${spring.kafka.topics.clear-cart.partitions}")
    private int clearCartPartitions;

    @Value("${spring.kafka.topics.clear-cart.replication-factor}")
    private short clearCartReplicationFactor;

    @Value("${spring.kafka.topics.clear-cart-dlt.name}")
    private String clearCartDltTopic;

    @Value("${spring.kafka.topics.clear-cart-dlt.partitions}")
    private int clearCartDltPartitions;

    @Value("${spring.kafka.topics.clear-cart-dlt.replication-factor}")
    private short clearCartDltReplicationFactor;

    @Bean
    public NewTopic clearCartTopic() {
        return new NewTopic(
                clearCartTopic,
                clearCartPartitions,
                clearCartReplicationFactor
        );
    }

    @Bean
    public NewTopic clearCartDLT() {
        return new NewTopic(
                clearCartDltTopic,
                clearCartDltPartitions,
                clearCartDltReplicationFactor
        );
    }
}
