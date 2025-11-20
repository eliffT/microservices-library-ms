package com.turkcell.bookservice.infrastructure.messaging.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.bookservice.application.ports.input.eventlistener.LoanCreatedEventListener;
import com.turkcell.bookservice.application.ports.input.eventlistener.LoanReturnedEventListener;
import com.turkcell.common.events.DomainEvent;
import com.turkcell.common.events.LoanCreatedEvent;
import com.turkcell.common.events.LoanReturnedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class LoanEventsConsumer {
    private final ObjectMapper objectMapper;
    private final LoanCreatedEventListener createdListener;
    private final LoanReturnedEventListener returnedListener;

    public LoanEventsConsumer(ObjectMapper objectMapper,
                              LoanCreatedEventListener createdListener,
                              LoanReturnedEventListener returnedListener) {
        this.objectMapper = objectMapper;
        this.createdListener = createdListener;
        this.returnedListener = returnedListener;
    }


    @KafkaListener(topics = "${kafka.topics.loan-events}",
            groupId = "book-service-loan-group")
    // Gelen mesajı JSON'dan String yerine, otomatik olarak DomainEvent'e eşleştir.
    public void consumeLoanCreated(@Payload DomainEvent event) {
        // @Payload'ın temel işlevi, gelen bayt veya String mesaj içeriğini (payload),
        // hedef metot parametresinin (bu örnekte DomainEvent event) Java tipine otomatik olarak dönüştürmektir.
        try {
            // Spring Kafka'nın Header Deserialization özelliği, event'i doğru tipe
            // (LoanCreatedEvent veya LoanReturnedEvent) zaten dönüştürmüş olmalı.

            if (event instanceof LoanCreatedEvent loanCreatedEvent) {
                System.out.println("INFO: LoanCreatedEvent alındı. Kitap ID: " + loanCreatedEvent.bookId());
                createdListener.handle(loanCreatedEvent);

            } else if (event instanceof LoanReturnedEvent loanReturnedEvent) {
                System.out.println("INFO: LoanReturnedEvent alındı. Kitap ID: " + loanReturnedEvent.bookId());
                returnedListener.handle(loanReturnedEvent);
            } else {
                // Topic'te beklemediğimiz bir DomainEvent tipi geldi.
                System.err.println("WARN: Bilinmeyen DomainEvent tipi atlanıyor: " + event.getClass().getSimpleName());
            }

        } catch (Exception e) {
            // BusinessException veya Database hatası
            System.err.println("CRITICAL ERROR: Loan Event işlenirken hata: " + e.getMessage());
            // Transactional Outbox/Inbox desenini takip edebilmek için RuntimeException fırlatılır.
            throw new RuntimeException("Loan Event işleme hatası: " + e.getMessage(), e);
        }

    }

}
