package com.turkcell.borrowservice.application.commands.handler;

import com.turkcell.borrowservice.application.ports.output.eventproducer.KafkaEventProducerPort;
import com.turkcell.borrowservice.domain.model.Reservation;
import com.turkcell.borrowservice.domain.model.enumstatus.ReservationStatus;
import com.turkcell.borrowservice.domain.repository.ReservationRepository;
import com.turkcell.borrowservice.infrastructure.messaging.relayer.OutboxEventPersister;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class FulfillNextReservationCommandHandler {
    private final ReservationRepository reservationRepository;
    private final KafkaEventProducerPort eventPublisher;
    private static final int PICKUP_HOURS = 24;

    public FulfillNextReservationCommandHandler(ReservationRepository reservationRepository,
                                                KafkaEventProducerPort eventPublisher) {
        this.reservationRepository = reservationRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void handle(UUID bookId) {

        // 1. Kitap için sıradaki ACTIVE rezervasyonu bul
        Optional<Reservation> nextReservation =
                reservationRepository.findFirstByBookIdAndStatusOrderByReservationDateAsc(bookId, ReservationStatus.ACTIVE);

        if (nextReservation.isPresent()) {
            Reservation reservation = nextReservation.get();

            // 2. DOMAIN ÇAĞRISI: Pickup penceresi ata
            reservation.fulfill(PICKUP_HOURS);

            // 3. AGGREGATE PERSISTENCE
            reservationRepository.save(reservation);

            // 4. OUTBOX KAYDI
            eventPublisher.publish(reservation.getDomainEvents());
            reservation.clearDomainEvents();
        }
    }

}
