package com.turkcell.borrowservice.infrastructure.messaging.outbox.relayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.borrowservice.infrastructure.messaging.kafka.KafkaProducerService;
import com.turkcell.borrowservice.infrastructure.messaging.kafka.KafkaTopicMapper;
import com.turkcell.borrowservice.infrastructure.messaging.outbox.OutboxMessage;
import com.turkcell.borrowservice.infrastructure.messaging.outbox.OutboxRepository;
import com.turkcell.borrowservice.infrastructure.messaging.outbox.OutboxStatus;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;


@Component
public class OutboxMessageRelayerJob {     // DB'den Read, Kafka'ya iletim ve durum güncelleme

    private static final int MAX_RETRY_COUNT = 5;

    private final OutboxRepository outboxRepository;
    private final KafkaProducerService kafkaProducerService;
    private final KafkaTopicMapper topicMapper;
    private final ObjectMapper objectMapper;

    // Tüm eventler 'com.turkcell.common.events.' paketi altında.
    private static final String EVENT_PACKAGE_PREFIX = "com.turkcell.common.events.";

    public OutboxMessageRelayerJob(OutboxRepository outboxRepository,
                                   KafkaProducerService kafkaProducerService,
                                   KafkaTopicMapper topicMapper, ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.topicMapper = topicMapper;
        this.objectMapper = objectMapper;
    }


    @Scheduled(fixedRate = 5000)
    @Transactional
    public void relayPendingMessages() {

        List<OutboxMessage> pendingMessages = outboxRepository.findByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);

        if (pendingMessages.isEmpty())
            return;

        for (OutboxMessage message : pendingMessages) {
            try {
                // Topic adını belirle
                String topicName = topicMapper.mapEventTypeToTopic(message.getEventType());

                // Key olarak Aggregate ID'yi kullan (Kafka'da sıralı işleme garantisi için)
                String key = message.getAggregateId().toString();

                // JSON String'ini Doğru DomainEvent Objesine Dönüştürülür
                String fullClassName = EVENT_PACKAGE_PREFIX + message.getEventType();
                Class<?> eventClass = Class.forName(fullClassName);

                // ObjectMapper kullanarak JSON String'i DomainEvent'e dönüştürülür
                Object domainEvent = objectMapper.readValue(message.getPayloadJson(), eventClass);


                // Bu çağrı, KafkaProducerService'deki Object payload'lu metodu kullanır.
                kafkaProducerService.sendMessage(topicName, key, domainEvent);

                message.setStatus(OutboxStatus.PROCESSED);
                message.setProcessedAt(OffsetDateTime.now());

            } catch (Exception e) {
                int newRetryCount = message.getRetryCount() + 1;
                message.setRetryCount(newRetryCount);

                if (newRetryCount >= MAX_RETRY_COUNT) {
                    message.setStatus(OutboxStatus.FAILED);
                    System.err.println("Maximum number of attempts reached.");
                } else {
                    System.err.println("Temporary error while sending to Kafka.");
                }
                System.err.println("Error while sending to Kafka: " + message.getId() + " - " + e.getMessage());
            }
        }
        outboxRepository.saveAll(pendingMessages);
    }


}
