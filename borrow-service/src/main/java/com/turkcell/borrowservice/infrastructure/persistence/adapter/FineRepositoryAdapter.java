package com.turkcell.borrowservice.infrastructure.persistence.adapter;

import com.turkcell.borrowservice.domain.model.DomainId;
import com.turkcell.borrowservice.domain.model.Fine;
import com.turkcell.borrowservice.domain.repository.FineRepository;
import com.turkcell.borrowservice.infrastructure.persistence.entity.FineEntity;
import com.turkcell.borrowservice.infrastructure.persistence.jparepository.FineJpaRepository;
import com.turkcell.borrowservice.infrastructure.mapper.FineEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class FineRepositoryAdapter implements FineRepository {

    private final FineJpaRepository jpaRepository;
    private final FineEntityMapper mapper;

    public FineRepositoryAdapter(FineJpaRepository jpaRepository, FineEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Fine> findById(DomainId<Fine> id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Fine save(Fine fine) {
        FineEntity jpaEntity = mapper.toEntity(fine);
        FineEntity savedJpaEntity = jpaRepository.save(jpaEntity);
        return mapper.toDomain(savedJpaEntity);
    }

    @Override
    public boolean existsByUserIdAndIsPaid(UUID userId, boolean isPaid) {
        return jpaRepository.existsByUserIdAndIsPaid(userId, isPaid);
    }

    @Override
    public List<Fine> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Fine> findByUserIdAndIsPaid(UUID userId, boolean isPaid) {
        return jpaRepository.findByUserIdAndIsPaid(userId, isPaid).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
