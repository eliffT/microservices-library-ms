package com.turkcell.bookservice.application.mapper;

import com.turkcell.bookservice.application.dto.PublisherResponse;
import com.turkcell.bookservice.domain.model.publisher.Publisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherResponseMapper {

    public PublisherResponse toResponse(Publisher publisher) {
        return new PublisherResponse(
                publisher.id().value(),
                publisher.name()
        );
    }
}
