package com.turkcell.borrowservice.infrastructure.messaging.kafka;

import org.springframework.stereotype.Component;

// Event tiplerini Kafka Topic adlarına eşleyen yardımcı sınıf

@Component
public class KafkaTopicMapper {

    public String mapEventTypeToTopic(String eventType) {
        if (eventType.startsWith("Loan")) return "loan-events";
        if (eventType.startsWith("Reservation")) return "reservation-events";
        if (eventType.startsWith("Fine")) return "fine-events";
        if (eventType.startsWith("BookStock")) return "inventory-events"; // Inventory olayları için topic
        return "default-events";
    }

}
