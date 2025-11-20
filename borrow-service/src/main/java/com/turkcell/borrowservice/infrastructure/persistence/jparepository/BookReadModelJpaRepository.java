package com.turkcell.borrowservice.infrastructure.persistence.jparepository;

import com.turkcell.borrowservice.infrastructure.persistence.entity.BookReadModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookReadModelJpaRepository extends JpaRepository<BookReadModelEntity, UUID> {

    // İş kuralı kontrolü için direkt sorgu
    boolean existsByBookIdAndIsAvailable(UUID bookId, boolean isAvailable);
}
