package com.turkcell.bookservice.application.publisher.mapper;

import com.turkcell.bookservice.application.publisher.dto.PublisherResponse;
import com.turkcell.bookservice.domain.model.publisher.Publisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherResponseMapper {

    public PublisherResponse toResponse(Publisher publisher) {
        return new PublisherResponse(
                publisher.getId().value(),
                publisher.getName()
        );
    }
}
