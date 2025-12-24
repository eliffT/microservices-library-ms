package com.turkcell.borrowservice.application.commands.handler;

import com.turkcell.borrowservice.application.commands.CreateReservationCommand;
import com.turkcell.borrowservice.application.exceptions.BusinessException;
import com.turkcell.borrowservice.application.ports.BookReadModelRepository;
import com.turkcell.borrowservice.application.ports.output.eventproducer.EventPublisher;
import com.turkcell.borrowservice.application.ports.output.external.UserQueryPort;
import com.turkcell.borrowservice.domain.model.Reservation;
import com.turkcell.borrowservice.domain.model.enumstatus.ReservationStatus;
import com.turkcell.borrowservice.domain.repository.FineRepository;
import com.turkcell.borrowservice.domain.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateReservationCommandHandler {
    private final ReservationRepository reservationRepository;
    private final EventPublisher eventPublisher;
    private final FineRepository fineRepository;
    private final UserQueryPort userQueryPort; // Feign Client
    private final BookReadModelRepository bookReadModelRepository;

    public CreateReservationCommandHandler(ReservationRepository reservationRepository,
                                           EventPublisher eventPublisher,
                                           FineRepository fineRepository,
                                           UserQueryPort userQueryPort,
                                           BookReadModelRepository bookReadModelRepository) {
        this.reservationRepository = reservationRepository;
        this.eventPublisher = eventPublisher;
        this.fineRepository = fineRepository;
        this.userQueryPort = userQueryPort;
        this.bookReadModelRepository = bookReadModelRepository;
    }

    @Transactional
    public UUID handle(CreateReservationCommand command) {


        // KRİTİK KURAL: BANNED KONTROLÜ (Senkron API Çağrısı)
        String memberStatus = userQueryPort.getMembershipLevel(command.userId());

        if (memberStatus.equals("BANNED")) {
            // Business Rule : BANNED üyeler ödünç/rezervasyon yapamaz.
            throw new BusinessException("User ID: " + command.userId() + " is BANNED and cannot create a reservation.");
        }

        // Kural A: Üyenin genel cezası varsa rezervasyon yapamaz.
        if (fineRepository.existsByUserIdAndIsPaid(command.userId(), false)) {
            throw new BusinessException("Rule Violation: The user has outstanding fines. Reservations cannot be made.");
        }

        // Kural B: Üyenin aynı kitap için ACTIVE rezervasyonu var mı?
        if (reservationRepository.existsByUserIdAndBookIdAndStatus(command.userId(), command.bookId(), ReservationStatus.ACTIVE)) {
            throw new BusinessException("Rule Violation: The user already has an active reservation for this book.");
        }

        // Kural C: availableCopies == 0 değilse RET (Stok varsa direkt ödünç alınmalı - Kural 1'in tersi)
        int availableStock = bookReadModelRepository.getAvailableStock(command.bookId());

        if (availableStock > 0) {
            // Business logic: Stok varsa rezervasyon yapılmaz, direkt ödünç alınır.
            throw new BusinessException("The book is currently in stock. Please borrow it.");
        }


        // DOMAIN ÇAĞRISI (Aggregate Oluşturma)
        // Yeni rezervasyon expireAt=null olarak oluşturulur.
        Reservation reservation = Reservation.create(command.userId(), command.bookId());

        // AGGREGATE PERSISTENCE
        Reservation savedReservation = reservationRepository.save(reservation);

        //  OUTBOX KAYDI
        eventPublisher.publish(reservation.getDomainEvents());
        reservation.clearDomainEvents();

        return savedReservation.id().value();
    }

}
