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
            // Business Rule 3: BANNED üyeler ödünç/rezervasyon yapamaz.
            throw new BusinessException("User ID: " + command.userId() + " is BANNED and cannot create a reservation.");
        }

        // Kural A: Üyenin genel cezası varsa rezervasyon yapamaz (Önceki kuralınız, geçerliliğini korur).
        if (fineRepository.existsByUserIdAndIsPaid(command.userId(), false)) {
            throw new BusinessException("Kural İhlali: Kullanıcının ödenmemiş cezası bulunmaktadır. Rezervasyon yapılamaz.");
        }

        // Kural B: Üyenin aynı kitap için ACTIVE rezervasyonu var mı? (Kural 2 ve 3)
        if (reservationRepository.existsByUserIdAndBookIdAndStatus(command.userId(), command.bookId(), ReservationStatus.ACTIVE)) {
            throw new BusinessException("Kural İhlali: Kullanıcının bu kitap için zaten aktif bir rezervasyonu bulunmaktadır.");
        }

        // Kural C: availableCopies == 0 değilse RET (Stok varsa direkt ödünç alınmalı - Kural 1'in tersi)
        int availableStock = bookReadModelRepository.getAvailableStock(command.bookId());

        if (availableStock > 0) {
            // Business logic: Stok varsa rezervasyon yapılmaz, direkt ödünç alınır.
            throw new BusinessException("Kitap şu an stokta mevcut. Lütfen ödünç alınız.");
        }


        // 2. DOMAIN ÇAĞRISI (Aggregate Oluşturma)
        // Yeni rezervasyon expireAt=null olarak oluşturulur.
        Reservation reservation = Reservation.create(command.userId(), command.bookId());

        // 3. AGGREGATE PERSISTENCE
        Reservation savedReservation = reservationRepository.save(reservation);

        // 4. OUTBOX KAYDI
        eventPublisher.publish(reservation.getDomainEvents());
        reservation.clearDomainEvents();

        return savedReservation.id().value();
    }

}
