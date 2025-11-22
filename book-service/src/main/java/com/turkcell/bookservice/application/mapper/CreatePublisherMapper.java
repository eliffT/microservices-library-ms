package com.turkcell.bookservice.application.mapper;

import com.turkcell.bookservice.application.commands.CreatePublisherCommand;
import com.turkcell.bookservice.application.dto.CreatedPublisherResponse;
import com.turkcell.bookservice.domain.model.publisher.Publisher;
import org.springframework.stereotype.Component;

@Component
public class CreatePublisherMapper {

    public Publisher toDomain(CreatePublisherCommand command) {
        return Publisher.create(command.name());
    }

    public CreatedPublisherResponse toResponse(Publisher publisher) {
        return new CreatedPublisherResponse(
                publisher.id().value(),
                publisher.name()
        );
    }
}