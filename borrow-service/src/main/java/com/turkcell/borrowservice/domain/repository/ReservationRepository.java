package com.turkcell.borrowservice.domain.repository;

import com.turkcell.borrowservice.domain.model.DomainId;
import com.turkcell.borrowservice.domain.model.Reservation;
import com.turkcell.borrowservice.domain.model.enumstatus.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {
    Optional<Reservation> findById(DomainId<Reservation> id);

    List<Reservation> findByUserId(UUID userId);

    Reservation save(Reservation reservation);

    // Üyenin aynı kitap için ACTIVE rezervasyon kontrolü.
    boolean existsByUserIdAndBookIdAndStatus(UUID userId, UUID bookId, ReservationStatus status);

    // İlk ACTIVE rezervasyonu bulmak.
    Optional<Reservation> findFirstByBookIdAndStatusOrderByReservationDateAsc(UUID bookId, ReservationStatus status);

    // Süresi dolan rezervasyonları bulmak.
    List<Reservation> findByStatusAndExpireDateBefore(ReservationStatus status, LocalDateTime now);

    void deleteByUUID(DomainId<Reservation> id);

    List<Reservation> findByUserIdAndStatus(UUID userId, ReservationStatus status);
}

