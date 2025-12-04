package com.turkcell.bookservice.application.commands.handler;

import com.turkcell.bookservice.application.commands.DeleteBookCommand;
import com.turkcell.bookservice.application.exceptions.NotFoundException;
import com.turkcell.bookservice.application.ports.output.eventproducer.EventPublisher;
import com.turkcell.bookservice.core.cqrs.CommandHandler;
import com.turkcell.bookservice.domain.model.DomainId;
import com.turkcell.bookservice.domain.model.book.Book;
import com.turkcell.bookservice.domain.repository.BookRepository;
import com.turkcell.common.events.BookDeletedEvent;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeleteBookCommandHandler implements CommandHandler<DeleteBookCommand, Void> {

    private final BookRepository bookRepository;
    private final EventPublisher eventPublisher;

    public DeleteBookCommandHandler(BookRepository bookRepository, EventPublisher eventPublisher) {
        this.bookRepository = bookRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public Void handle(DeleteBookCommand command) {
        DomainId<Book> bookDomainId = DomainId.from(command.bookId());

        bookRepository.findById(bookDomainId)
                .orElseThrow(() -> new NotFoundException("Book not found with ID: " + command.bookId()));

        bookRepository.delete(bookDomainId);

        BookDeletedEvent event = new BookDeletedEvent(command.bookId());
        eventPublisher.publish(List.of(event));

        return null; // Void dönüş tipi için
    }
}
