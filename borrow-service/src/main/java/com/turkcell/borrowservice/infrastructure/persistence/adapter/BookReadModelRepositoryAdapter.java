package com.turkcell.borrowservice.infrastructure.persistence.adapter;

import com.turkcell.borrowservice.application.ports.BookReadModelRepository;
import com.turkcell.borrowservice.application.ports.output.BookReadModel;
import com.turkcell.borrowservice.infrastructure.mapper.BookModelEntityMapper;
import com.turkcell.borrowservice.infrastructure.persistence.entity.readmodel.BookReadModelEntity;
import com.turkcell.borrowservice.infrastructure.persistence.jparepository.BookReadModelJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class BookReadModelRepositoryAdapter implements BookReadModelRepository {
    private final BookReadModelJpaRepository jpaRepository;
    private final BookModelEntityMapper mapper;

    public BookReadModelRepositoryAdapter(BookReadModelJpaRepository jpaRepository, BookModelEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<BookReadModel> findById(UUID bookId) {
        return jpaRepository.findById(bookId).map(mapper::toDomain);
    }

    @Override
    public BookReadModel save(BookReadModel model) {
        BookReadModelEntity entity = mapper.toEntity(model);
        BookReadModelEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }


    @Override
    public int getAvailableStock(UUID bookId) {
        return jpaRepository.findById(bookId)
                .map(BookReadModelEntity::availableStock)
                .orElse(0);
    }

    @Override
    public boolean isBookAvailable(UUID bookId) {
        return jpaRepository.existsByBookIdAndIsAvailable(bookId, true);
    }

    @Override
    public void deleteById(UUID bookId) {
        jpaRepository.deleteById(bookId);
    }


}
