package com.turkcell.bookservice.infrastructure.jparepository;

import com.turkcell.bookservice.infrastructure.entity.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PublisherJpaRepository extends JpaRepository<PublisherEntity, UUID> {
}
