package com.turkcell.borrowservice.infrastructure.persistence.adapter;

import com.turkcell.borrowservice.domain.model.DomainId;
import com.turkcell.borrowservice.domain.model.Loan;
import com.turkcell.borrowservice.domain.model.enumstatus.LoanStatus;
import com.turkcell.borrowservice.domain.repository.LoanRepository;
import com.turkcell.borrowservice.infrastructure.persistence.entity.LoanEntity;
import com.turkcell.borrowservice.infrastructure.persistence.jparepository.LoanJpaRepository;
import com.turkcell.borrowservice.infrastructure.mapper.LoanEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class LoanRepositoryAdapter implements LoanRepository {

    private final LoanJpaRepository jpaRepository;
    private final LoanEntityMapper mapper;

    public LoanRepositoryAdapter(LoanJpaRepository jpaRepository, LoanEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Loan> findById(DomainId<Loan> id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Loan save(Loan loan) {
        LoanEntity jpaEntity = mapper.toEntity(loan);
        LoanEntity savedJpaEntity = jpaRepository.save(jpaEntity);
        return mapper.toDomain(savedJpaEntity);
    }

    @Override
    public Optional<Loan> findByUserIdAndBookIdAndStatus(UUID userId, UUID bookId, LoanStatus status) {
        return jpaRepository.findByUserIdAndBookIdAndStatus(userId, bookId, status)
                .map(mapper::toDomain);
    }

    @Override
    public List<Loan> findByUserIdAndStatusIn(UUID userId, List<LoanStatus> statuses) {
        return jpaRepository.findByUserIdAndStatusIn(userId, statuses).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByStatus(LoanStatus status) {
        return jpaRepository.findByStatus(status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(DomainId<Loan> id) {
        jpaRepository.deleteById(id.value());
    }


    @Override
    public List<Loan> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}
