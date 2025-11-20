package com.turkcell.borrowservice.application.commands.handler;

import com.turkcell.borrowservice.application.commands.CancelReservationCommand;

import com.turkcell.borrowservice.application.exceptions.NotFoundException;
import com.turkcell.borrowservice.application.ports.output.eventproducer.KafkaEventProducerPort;
import com.turkcell.borrowservice.domain.model.DomainId;
import com.turkcell.borrowservice.domain.model.Reservation;
import com.turkcell.borrowservice.domain.repository.ReservationRepository;
import com.turkcell.borrowservice.infrastructure.messaging.relayer.OutboxEventPersister;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CancelReservationCommandHandler {
    private final ReservationRepository reservationRepository;
    private final KafkaEventProducerPort eventPublisher;
    private final FulfillNextReservationCommandHandler fulfillNextHandler;

    public CancelReservationCommandHandler(ReservationRepository reservationRepository,
                                           KafkaEventProducerPort eventPublisher,
                                           FulfillNextReservationCommandHandler fulfillNextHandler) {
        this.reservationRepository = reservationRepository;
        this.eventPublisher = eventPublisher;
        this.fulfillNextHandler = fulfillNextHandler;
    }

    @Transactional
    public void handle(CancelReservationCommand command) {

        Reservation reservation = reservationRepository.findById(new DomainId<Reservation>(command.reservationId()))
                .orElseThrow(() -> new NotFoundException("Reservation not found with ID: " + command.reservationId()));

        UUID bookId = reservation.bookId();

        // Bu çağrı içinde ReservationCancelledEvent("Manual") tetiklenir.
        reservation.cancel();
        reservationRepository.save(reservation);

        eventPublisher.publish(reservation.getDomainEvents());
        reservation.clearDomainEvents();

        // Sıradaki Rezervasyonu Kontrol Etme
        // İptal edilen rezervasyonun yerini doldurmak için sıradaki rezervasyonu aktive etmeyi dener.
        fulfillNextHandler.handle(bookId);
    }
}
