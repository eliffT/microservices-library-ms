package com.turkcell.borrowservice.infrastructure.messaging.kafka.consumer;

import com.turkcell.borrowservice.application.queries.handler.listener.InventoryUpdateListener;
import com.turkcell.common.events.BookCreatedEvent;
import com.turkcell.common.events.BookStockChangedEvent;
import com.turkcell.common.events.DomainEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Book servisinden gelen BookStockChangedEvent olaylarını dinler.
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
    // @Payload kullanarak doğrudan DomainEvent nesnesini alınır
    public void consumeInventoryEvents(@Payload DomainEvent event) {
        try {
            // Spring Kafka, __TypeId__ başlığı sayesinde event'i doğru tipe dönüştürülür

            if (event instanceof BookStockChangedEvent bookStockChangedEvent) {

                inventoryUpdateListener.handle(bookStockChangedEvent);
                System.out.printf("INFO: Successfully processed BookStockChangedEvent for book ID: %s%n", bookStockChangedEvent.aggregateId());

            } else if (event instanceof BookCreatedEvent bookCreatedEvent) {

                inventoryUpdateListener.handle(bookCreatedEvent);
                System.out.printf("INFO: Successfully processed BookCreatedEvent for book ID: %s%n", bookCreatedEvent.getAggregateId());

            } else {
                // Topic'te beklemediğimiz bir event tipi geldi.
                System.err.println("WARN: Bilinmeyen Inventory Event tipi atlanıyor: " + event.getClass().getSimpleName());
            }

        } catch (Exception e) {
            System.err.println("CRITICAL ERROR: Failed to process event in InventoryUpdateListener: " + e.getMessage());
            // RuntimeException fırlatılması, Kafka'da retry mekanizmasını tetikler.
            throw new RuntimeException("Inventory Event işlenirken hata oluştu.", e);
        }
    }

}
