package com.turkcell.bookservice.infrastructure.messaging.outbox.relayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.bookservice.infrastructure.messaging.kafka.KafkaProducerService;
import com.turkcell.bookservice.infrastructure.messaging.kafka.KafkaTopicMapper;
import com.turkcell.bookservice.infrastructure.messaging.outbox.OutboxMessage;
import com.turkcell.bookservice.infrastructure.messaging.outbox.OutboxRepository;
import com.turkcell.bookservice.infrastructure.messaging.outbox.OutboxStatus;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class OutboxMessageRelayerJob {

    private static final int MAX_RETRY_COUNT = 5;

    private final OutboxRepository outboxRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ObjectMapper objectMapper;
    private final KafkaTopicMapper topicMapper;

    //Tüm eventler 'com.turkcell.common.events.' paketi altında.
    private static final String EVENT_PACKAGE_PREFIX = "com.turkcell.common.events.";

    public OutboxMessageRelayerJob(OutboxRepository outboxRepository,
                                   KafkaProducerService kafkaProducerService,
                                   ObjectMapper objectMapper, KafkaTopicMapper topicMapper) {
        this.outboxRepository = outboxRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.objectMapper = objectMapper;
        this.topicMapper = topicMapper;
    }


    @Scheduled(fixedRate = 5000)
    @Transactional
    public void relayPendingMessages() {

        List<OutboxMessage> pendingMessages = outboxRepository.findByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);

        if (pendingMessages.isEmpty())
            return;

        for (OutboxMessage message : pendingMessages) {
            try {
                // Topic adını belirlenir.
                String topicName = topicMapper.mapAggregateTypeToTopic(message.getAggregateType());

                // Key olarak Aggregate ID'yi kullanılır (Kafka'da sıralı işleme garantisi için)
                String key = message.getAggregateId().toString();

                // JSON String'ini Doğru DomainEvent Objesine Dönüştürülür.
                String fullClassName = EVENT_PACKAGE_PREFIX + message.getEventType();
                Class<?> eventClass = Class.forName(fullClassName);

                // ObjectMapper kullanarak JSON String'i DomainEvent'e dönüştürülür.
                Object domainEvent = objectMapper.readValue(message.getPayloadJson(), eventClass);

                kafkaProducerService.sendMessage(topicName, key, domainEvent);

                message.setStatus(OutboxStatus.PROCESSED);
                message.setProcessedAt(OffsetDateTime.now());

            } catch (Exception e) {
                int newRetryCount = message.getRetryCount() + 1;
                message.setRetryCount(newRetryCount);

                if (newRetryCount >= MAX_RETRY_COUNT)
                    message.setStatus(OutboxStatus.FAILED);

            }
        }
        outboxRepository.saveAll(pendingMessages);
    }
}
