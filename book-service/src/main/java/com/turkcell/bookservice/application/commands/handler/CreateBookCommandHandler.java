package com.turkcell.bookservice.application.commands.handler;

import com.turkcell.bookservice.application.commands.CreateBookCommand;
import com.turkcell.bookservice.application.dto.CreatedBookResponse;
import com.turkcell.bookservice.application.mapper.CreateBookMapper;
import com.turkcell.bookservice.application.ports.output.eventproducer.EventPublisher;
import com.turkcell.bookservice.core.cqrs.CommandHandler;
import com.turkcell.bookservice.domain.model.book.Book;
import com.turkcell.bookservice.domain.repository.BookRepository;
import com.turkcell.common.events.DomainEvent;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateBookCommandHandler implements CommandHandler<CreateBookCommand, CreatedBookResponse> {

    private final BookRepository bookRepository;
    private final CreateBookMapper mapper;
    private final EventPublisher eventPublisher;

    public CreateBookCommandHandler(BookRepository bookRepository,
                                    CreateBookMapper mapper,
                                    EventPublisher eventPublisher) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public CreatedBookResponse handle(CreateBookCommand command) {
        Book book = mapper.toDomain(command);

        List<DomainEvent> domainEvents = book.getDomainEvents();
        book = bookRepository.save(book);

        eventPublisher.publish(domainEvents);
        book.clearDomainEvents();
        return mapper.toResponse(book);
    }
}
