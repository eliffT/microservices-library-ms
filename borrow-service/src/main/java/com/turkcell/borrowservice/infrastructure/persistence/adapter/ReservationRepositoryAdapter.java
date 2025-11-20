package com.turkcell.borrowservice.infrastructure.persistence.adapter;

import com.turkcell.borrowservice.domain.model.DomainId;
import com.turkcell.borrowservice.domain.model.Reservation;
import com.turkcell.borrowservice.domain.model.enumstatus.ReservationStatus;
import com.turkcell.borrowservice.domain.repository.ReservationRepository;
import com.turkcell.borrowservice.infrastructure.persistence.entity.ReservationEntity;
import com.turkcell.borrowservice.infrastructure.persistence.jparepository.ReservationJpaRepository;
import com.turkcell.borrowservice.infrastructure.mapper.ReservationEntityMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ReservationRepositoryAdapter implements ReservationRepository {

    private final ReservationJpaRepository jpaRepository;
    private final ReservationEntityMapper mapper;

    public ReservationRepositoryAdapter(ReservationJpaRepository jpaRepository, ReservationEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Reservation> findById(DomainId<Reservation> id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public List<Reservation> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Reservation save(Reservation reservation) {
        ReservationEntity jpaEntity = mapper.toEntity(reservation);
        ReservationEntity savedJpaEntity = jpaRepository.save(jpaEntity);
        return mapper.toDomain(savedJpaEntity);
    }

    @Override
    public boolean existsByUserIdAndBookIdAndStatus(UUID userId, UUID bookId, ReservationStatus status) {
        return jpaRepository.existsByUserIdAndBookIdAndStatus(userId, bookId, status);
    }

    @Override
    public Optional<Reservation> findFirstByBookIdAndStatusOrderByReservationDateAsc(UUID bookId, ReservationStatus status) {
        return jpaRepository.findByBookIdAndStatusOrderByReservationDateAsc(bookId, status)
                .map(mapper::toDomain);
    }

    @Override
    public List<Reservation> findByStatusAndExpireDateBefore(ReservationStatus status, LocalDateTime now) {
        return jpaRepository.findByStatusAndExpireDateBefore(status, now).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByUUID(DomainId<Reservation> id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public List<Reservation> findByUserIdAndStatus(UUID userId, ReservationStatus status) {
        return jpaRepository.findByUserIdAndStatus(userId, status)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }


}
