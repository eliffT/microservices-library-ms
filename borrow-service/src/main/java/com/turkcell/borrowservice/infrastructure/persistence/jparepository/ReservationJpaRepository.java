package com.turkcell.borrowservice.infrastructure.persistence.jparepository;

import com.turkcell.borrowservice.domain.model.enumstatus.ReservationStatus;
import com.turkcell.borrowservice.infrastructure.persistence.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, UUID> {

    // Üyenin aynı kitap için ACTIVE rezervasyonu birden fazla olamaz.
    boolean existsByUserIdAndBookIdAndStatus(UUID userId, UUID bookId, ReservationStatus status);

    // İlk ACTIVE rezervasyonu bulmak için.
    Optional<ReservationEntity> findByBookIdAndStatusOrderByReservationDateAsc(UUID bookId, ReservationStatus status);

    // Süresi dolan rezervasyonları bulmak (Scheduled Job için).
    List<ReservationEntity> findByStatusAndExpireDateBefore(ReservationStatus status, LocalDateTime now);

    List<ReservationEntity> findByUserIdAndStatus(UUID userId, ReservationStatus status);

    List<ReservationEntity> findByUserId(UUID userId);
}