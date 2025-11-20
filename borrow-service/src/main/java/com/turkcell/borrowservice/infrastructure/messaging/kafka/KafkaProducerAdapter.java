package com.turkcell.borrowservice.infrastructure.messaging.kafka;


import com.turkcell.borrowservice.application.ports.output.eventproducer.KafkaEventProducerPort;
import com.turkcell.borrowservice.infrastructure.messaging.relayer.OutboxEventPersister;
import com.turkcell.common.events.DomainEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaProducerAdapter implements KafkaEventProducerPort {

    private final OutboxEventPersister outboxEventPersister;

    public KafkaProducerAdapter(OutboxEventPersister outboxEventPersister) {
        this.outboxEventPersister = outboxEventPersister;
    }

    @Override
    public void publish(List<DomainEvent> events) {
        outboxEventPersister.save(events);
    }

}
