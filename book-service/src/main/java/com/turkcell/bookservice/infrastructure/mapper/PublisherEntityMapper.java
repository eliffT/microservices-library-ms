package com.turkcell.bookservice.infrastructure.mapper;

import com.turkcell.bookservice.domain.model.DomainId;
import com.turkcell.bookservice.domain.model.publisher.Publisher;
import com.turkcell.bookservice.infrastructure.entity.PublisherEntity;
import org.springframework.stereotype.Component;

@Component
public class PublisherEntityMapper {

    public PublisherEntity toEntity(Publisher publisher) {
        PublisherEntity publisherEntity = new PublisherEntity();
        publisherEntity.setId(publisher.getId().value());
        publisherEntity.setName(publisher.getName());
        return publisherEntity;
    }

    public Publisher toDomain(PublisherEntity entity) {
        return Publisher.rehydrate(
                new DomainId<Publisher>(entity.id()),
                entity.name()
        );
    }
}
