package com.turkcell.bookservice.application.commands.handler;

import com.turkcell.bookservice.application.commands.CreatePublisherCommand;
import com.turkcell.bookservice.application.dto.CreatedPublisherResponse;
import com.turkcell.bookservice.application.mapper.CreatePublisherMapper;
import com.turkcell.bookservice.core.cqrs.CommandHandler;
import com.turkcell.bookservice.domain.model.publisher.Publisher;
import com.turkcell.bookservice.domain.repository.PublisherRepository;
import org.springframework.stereotype.Component;

@Component
public class CreatePublisherCommandHandler implements CommandHandler<CreatePublisherCommand, CreatedPublisherResponse> {

    private final PublisherRepository publisherRepository;
    private final CreatePublisherMapper mapper;

    public CreatePublisherCommandHandler(PublisherRepository publisherRepository, CreatePublisherMapper mapper) {
        this.publisherRepository = publisherRepository;
        this.mapper = mapper;
    }

    @Override
    public CreatedPublisherResponse handle(CreatePublisherCommand command) {
        Publisher publisher = mapper.toDomain(command);
        publisher = publisherRepository.save(publisher);
        return mapper.toResponse(publisher);
    }
}

