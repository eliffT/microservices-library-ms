package com.turkcell.bookservice.infrastructure.persistence.jparepository;

import com.turkcell.bookservice.infrastructure.persistence.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookJpaRepository extends JpaRepository<BookEntity, UUID>{
}
