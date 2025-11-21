package com.turkcell.bookservice.application.commands.handler;

import com.turkcell.bookservice.application.commands.BookStockCommand;
import com.turkcell.bookservice.application.ports.input.eventlistener.LoanReturnedEventListener;
import com.turkcell.bookservice.application.ports.output.eventproducer.EventPublisher;
import com.turkcell.bookservice.domain.model.DomainId;
import com.turkcell.bookservice.domain.model.book.Book;
import com.turkcell.bookservice.domain.repository.BookRepository;
import com.turkcell.bookservice.infrastructure.messaging.inbox.InboxMessage;
import com.turkcell.bookservice.infrastructure.messaging.inbox.InboxRepository;
import com.turkcell.common.events.DomainEvent;
import com.turkcell.common.events.LoanReturnedEvent;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IncreaseBookStockCommandHandler implements LoanReturnedEventListener {

    private final BookRepository bookRepository;
    private final InboxRepository inboxRepository;
    private final EventPublisher eventPublisher;

    public IncreaseBookStockCommandHandler(BookRepository bookRepository,
                                           InboxRepository inboxRepository,
                                           EventPublisher eventPublisher) {
        this.bookRepository = bookRepository;
        this.inboxRepository = inboxRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(LoanReturnedEvent event) {
        // Event'i Command'a dönüştürülür (Command'ın eventId'yi taşıdığını varsayıyoruz)
        BookStockCommand command = new BookStockCommand(
                event.bookId(),
                event.eventId()
        );
        this.handle(command);
    }

    /**
     * Kitap iade komutunu işler.
     * Stok artışı ve Event Outbox kaydı aynı @Transactional işlem içinde yapılır.
     * * @param command İşlenecek ReturnBookCommand.
     */
    @Transactional
    public void handle(BookStockCommand command) {
        try {
            // INBOX KONTROLÜ VE KAYDI (Idempotency)
            // Aynı event ID'si gelirse DataIntegrityViolationException fırlatır.
            InboxMessage inboxMessage = new InboxMessage(command.eventId(), command.bookId(), "BOOK");
            inboxRepository.saveAndFlush(inboxMessage);

            // STOK ARTIŞI
            Book book = bookRepository.findById(DomainId.from(command.bookId()))
                    .orElseThrow(() -> new IllegalArgumentException("İade edilecek kitap bulunamadı."));

            book.increaseStock(1); // Domain mantığı ile stok artışı

            // VERİTABANINA KAYIT
            bookRepository.save(book);

            // YENİ OLAY YAYIMLAMA: BookStockChangedEvent
            List<DomainEvent> events = book.getDomainEvents();
            if (!events.isEmpty()) {
                eventPublisher.publish(events);
            }
            book.clearDomainEvents();

        } catch (DataIntegrityViolationException e) {
            // Aynı olay ID'si zaten işlenmiş. İşlem atomik olarak atlanır.
            System.err.println("WARN: LoanReturnedEvent zaten işlenmiş, stok artışı atlandı. ID: " + command.eventId());
        } catch (Exception e) {
            // Kritik hata: Kafka'da retry tetiklenir.
            throw new RuntimeException("Stok artırma işlemi başarısız.", e);
        }
    }


}