package com.turkcell.bookservice.infrastructure.messaging.kafka;

import com.turkcell.bookservice.application.ports.output.eventproducer.EventPublisher;
import com.turkcell.bookservice.infrastructure.messaging.outbox.relayer.OutboxEventPersister;
import com.turkcell.common.events.DomainEvent;
import org.springframework.stereotype.Component;

import java.util.List;

// EventPublisher arayüzünü uygulayarak Domain ile Infrastructure katmanını ayırır.
// Event'leri doğrudan Kafka'ya göndermek yerine OutboxEventPersister'a yönlendirerek Outbox prensibi uygulanır.

@Component
public class KafkaProducerAdapter implements EventPublisher {

    private final OutboxEventPersister outboxEventPersister;

    public KafkaProducerAdapter(OutboxEventPersister outboxEventPersister) {
        this.outboxEventPersister = outboxEventPersister;
    }

    @Override
    public void publish(List<DomainEvent> events) {
        outboxEventPersister.save(events);
    }

}
