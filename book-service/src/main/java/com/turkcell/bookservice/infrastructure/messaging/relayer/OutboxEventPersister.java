package com.turkcell.bookservice.infrastructure.messaging.relayer;


import com.turkcell.common.events.DomainEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.turkcell.bookservice.infrastructure.messaging.outbox.OutboxMessage;
import com.turkcell.bookservice.infrastructure.messaging.outbox.OutboxRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OutboxEventPersister {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public OutboxEventPersister(OutboxRepository outboxRepository, ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    // Command Handler tarafından aynı Transaction içinde çağrılacak metot
    public void save(List<DomainEvent> domainEvents) {
        List<OutboxMessage> messages = domainEvents.stream()
                .map(this::toOutboxMessage)
                .collect(Collectors.toList());

        outboxRepository.saveAll(messages);
    }

    private OutboxMessage toOutboxMessage(DomainEvent event) {
        try {
            OutboxMessage message = new OutboxMessage();
            message.setEventId(event.getEventId());
            message.setAggregateId(event.getAggregateId());
            message.setAggregateType(event.getAggregateType().toString());
            message.setEventType(event.getClass().getSimpleName());

            // Domain Event'i JSON string'e serileştirme
            message.setPayloadJson(objectMapper.writeValueAsString(event));

            return message;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize domain event: " + event.getEventId(), e);
        }
    }
}
