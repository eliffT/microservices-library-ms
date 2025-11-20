package com.turkcell.borrowservice.infrastructure.messaging.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.borrowservice.application.queries.handler.listener.InventoryUpdateListener;
import com.turkcell.common.events.BookStockChangedEvent;
import com.turkcell.common.events.DomainEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Inventory servisinden gelen BookStockChangedEvent olaylarını dinler.
 * Spring Kafka, Header Deserialization kullanarak payload'ı doğrudan BookStockChangedEvent DTO'suna dönüştürür.
 */

@Component
public class BookStatusKafkaConsumer {

    private final InventoryUpdateListener inventoryUpdateListener;

    public BookStatusKafkaConsumer(InventoryUpdateListener inventoryUpdateListener) {
        this.inventoryUpdateListener = inventoryUpdateListener;
    }

    @KafkaListener(topics = "${kafka.topics.inventory}", // application.properties'den okunacak topic
            groupId = "${spring.kafka.consumer.group-id}")
    // @Payload kullanarak doğrudan BookStockChangedEvent nesnesini alınır
    public void consumeBookStockChanges(@Payload BookStockChangedEvent event) {
        try {
            // 1. Olay, @Payload sayesinde otomatik olarak dönüştürüldü.
            // Bu, aynı zamanda INBOX desenini uygulamadan önce tek bir olay tipi olduğu için kabul edilebilir.

            // 2. Application Katmanındaki Listener'ı çağır
            inventoryUpdateListener.handle(event);

            System.out.printf("INFO: Successfully processed BookStockChangedEvent for book ID: %s%n", event.aggregateId());

        } catch (Exception e) {
            // Listener'daki (iş mantığındaki) beklenmeyen hatalar
            System.err.println("CRITICAL ERROR: Failed to process event in InventoryUpdateListener: " + e.getMessage());
            // RuntimeException fırlatılması, Kafka'da retry mekanizmasını tetikler.
            throw new RuntimeException("Book Stock Changed Event işlenirken hata oluştu.", e);
        }
    }

}
