package com.turkcell.bookservice.infrastructure.persistence.jparepository;

import com.turkcell.bookservice.infrastructure.persistence.entity.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PublisherJpaRepository extends JpaRepository<PublisherEntity, UUID> {
}
