package com.turkcell.user_service.infrastructure.messaging.kafka.consumer;

import com.turkcell.common.events.DomainEvent;
import com.turkcell.common.events.FineCreatedEvent;
import com.turkcell.common.events.FinePaidEvent;
import com.turkcell.user_service.application.ports.input.eventlistener.FineEventListener;
import com.turkcell.user_service.infrastructure.messaging.inbox.InboxMessage;
import com.turkcell.user_service.infrastructure.messaging.inbox.InboxRepository;
import jakarta.transaction.Transactional;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class FineEventsConsumer {
    private final FineEventListener fineEventListener;
    private final InboxRepository inboxRepository;

    public FineEventsConsumer(FineEventListener fineEventListener, InboxRepository inboxRepository) {
        this.fineEventListener = fineEventListener;
        this.inboxRepository = inboxRepository;
    }

    @KafkaListener(topics = "fine-events",
            groupId = "${spring.kafka.consumer.group-id}")
    @Transactional // Inbox kaydı ve iş mantığı tek bir DB transaction'ında olmalıdır
    public void consumeFineEvents(@Payload DomainEvent event) {

        if (!(event instanceof DomainEvent domainEvent)) {
            System.err.println("WARN: Gelen mesaj DomainEvent tipi değil. Atlanıyor: " + event.getClass().getSimpleName());
            return;
        }

        UUID eventId = domainEvent.getEventId();
        String eventType = domainEvent.getClass().getSimpleName();

        // 1. Inbox Kontrolü (Idempotency)
        if (inboxRepository.findById(eventId).isPresent()) {
            System.out.printf("INFO: Event ID: %s (%s) daha önce işlenmiş. Tekrarlı mesaj atlanıyor.%n", eventId, eventType);
            return;
        }

        try {
            // 2. Event Tipine Göre İşleme ve Application Katmanına Delege Etme
            if (event instanceof FineCreatedEvent fineCreatedEvent) {
                fineEventListener.handleFineCreated(fineCreatedEvent);

            } else if (event instanceof FinePaidEvent finePaidEvent) {
                fineEventListener.handleFinePaid(finePaidEvent);

            } else {
                System.err.printf("WARN: Bilinmeyen Fine Event tipi (%s) geldi. İşlenmiyor.%n", eventType);
                return; // Bilinmeyen tipi kaydetmeden atla
            }

            // 3. Başarılı İşlemden Sonra Inbox Kaydını Ekle
            InboxMessage inboxMessage = new InboxMessage(eventId, domainEvent.getAggregateId(), eventType, OffsetDateTime.now());
            inboxRepository.save(inboxMessage);

            System.out.printf("SUCCESS: Event (%s - %s) başarıyla işlendi ve Inbox'a kaydedildi.%n", eventType, eventId);

        } catch (Exception e) {
            System.err.printf("CRITICAL ERROR: Event (%s - %s) işlenirken hata oluştu: %s%n", eventType, eventId, e.getMessage());
            // Transactional olduğu için, hata durumunda hem DB değişikliği hem de Inbox kaydı geri alınır.
            throw new RuntimeException("Event işlenirken hata oluştu, Kafka retry tetikleniyor.", e);
        }
    }
}
