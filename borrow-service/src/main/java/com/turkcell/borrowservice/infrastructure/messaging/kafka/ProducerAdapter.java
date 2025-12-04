package com.turkcell.borrowservice.infrastructure.messaging.kafka;


import com.turkcell.borrowservice.application.ports.output.eventproducer.EventPublisher;
import com.turkcell.borrowservice.infrastructure.messaging.outbox.relayer.OutboxEventPersister;
import com.turkcell.common.events.DomainEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducerAdapter implements EventPublisher {

    private final OutboxEventPersister outboxEventPersister;

    public ProducerAdapter(OutboxEventPersister outboxEventPersister) {
        this.outboxEventPersister = outboxEventPersister;
    }

    @Override
    public void publish(List<DomainEvent> events) {
        outboxEventPersister.save(events);
    }

}
