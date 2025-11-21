package com.turkcell.bookservice.application.commands.handler;

import com.turkcell.bookservice.application.commands.BookStockCommand;
import com.turkcell.bookservice.application.ports.input.eventlistener.LoanCreatedEventListener;
import com.turkcell.bookservice.application.ports.output.eventproducer.EventPublisher;
import com.turkcell.bookservice.domain.model.DomainId;
import com.turkcell.bookservice.domain.model.book.Book;
import com.turkcell.bookservice.domain.repository.BookRepository;
import com.turkcell.bookservice.infrastructure.messaging.inbox.InboxMessage;
import com.turkcell.bookservice.infrastructure.messaging.inbox.InboxRepository;
import com.turkcell.common.events.DomainEvent;
import com.turkcell.common.events.LoanCreatedEvent;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DecreaseBookStockCommandHandler implements LoanCreatedEventListener {
    private final BookRepository bookRepository;
    private final EventPublisher eventPublisher;
    private final InboxRepository inboxRepository;  // Idempotency için

    public DecreaseBookStockCommandHandler(BookRepository bookRepository,
                                           EventPublisher eventPublisher,
                                           InboxRepository inboxRepository) {
        this.bookRepository = bookRepository;
        this.eventPublisher = eventPublisher;
        this.inboxRepository = inboxRepository;
    }

    @Override
    public void handle(LoanCreatedEvent event) {
        // Event'i Command'a dönüştürülür
        BookStockCommand command = new BookStockCommand(
                event.bookId(),
                event.eventId() // Event ID'sini Command'a taşınır
        );
        this.handle(command);
    }

    @Transactional
    public void handle(BookStockCommand command) {
        try {
            // INBOX KONTROLÜ VE KAYDI (Idempotency)
            // LoanCreatedEvent'in iki kez stok düşürmesini engeller.
            InboxMessage inboxMessage = new InboxMessage(command.eventId(), command.bookId(), "BOOK");
            inboxRepository.saveAndFlush(inboxMessage);

            //  STOK DÜŞÜŞÜ
            Book book = bookRepository.findById(DomainId.from(command.bookId()))
                    .orElseThrow(() -> new IllegalArgumentException("Kitap bulunamadı."));

            book.decreaseStock(1); // Domain mantığı ile stok düşüşü

            // VERİTABANINA KAYIT
            bookRepository.save(book);

            // YENİ OLAY YAYIMLAMA: BookStockChangedEvent
            List<DomainEvent> events = book.getDomainEvents();
            if (!events.isEmpty()) {
                eventPublisher.publish(events);
            }
            book.clearDomainEvents();

        } catch (DataIntegrityViolationException e) {
            // Aynı olay ID'si zaten işlenmiş. İşlem atlandı.
            System.err.println("WARN: LoanCreatedEvent zaten işlenmiş, stok düşüşü atlandı. ID: " + command.eventId());
        } catch (Exception e) {
            // Kritik hata: Kafka'da retry tetiklenir.
            throw new RuntimeException("Stok düşürme işlemi başarısız.", e);
        }
    }


}
