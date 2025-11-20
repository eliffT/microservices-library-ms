package com.turkcell.bookservice.infrastructure.messaging.kafka;

import org.springframework.stereotype.Component;

@Component
public class KafkaTopicMapper {

    public String mapAggregateTypeToTopic(String aggregateType) {
        return switch (aggregateType) {
            case "BOOK", "CATEGORY", "AUTHOR", "PUBLISHER" -> "inventory-events";
            default -> "default-events";
        };
    }
}
