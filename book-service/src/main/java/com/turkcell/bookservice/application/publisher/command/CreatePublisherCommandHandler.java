package com.turkcell.bookservice.application.publisher.command;

import com.turkcell.bookservice.application.publisher.dto.CreatedPublisherResponse;
import com.turkcell.bookservice.application.publisher.mapper.CreatePublisherMapper;
import com.turkcell.bookservice.core.cqrs.CommandHandler;
import com.turkcell.bookservice.domain.model.publisher.Publisher;
import com.turkcell.bookservice.domain.repository.PublisherRepository;
import org.springframework.stereotype.Component;

@Component
public class CreatePublisherCommandHandler implements CommandHandler<CreatePublisherCommand, CreatedPublisherResponse> {

    private final PublisherRepository publisherRepository;
    private final CreatePublisherMapper createPublisherMapper;

    public CreatePublisherCommandHandler(PublisherRepository publisherRepository, CreatePublisherMapper createPublisherMapper) {
        this.publisherRepository = publisherRepository;
        this.createPublisherMapper = createPublisherMapper;
    }

    @Override
    public CreatedPublisherResponse handle(CreatePublisherCommand command) {
        Publisher publisher = createPublisherMapper.toDomain(command);
        publisher = publisherRepository.save(publisher);
        return createPublisherMapper.toResponse(publisher);
    }
}

