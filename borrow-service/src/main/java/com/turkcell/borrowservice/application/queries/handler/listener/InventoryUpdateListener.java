package com.turkcell.borrowservice.application.queries.handler.listener;

import com.turkcell.borrowservice.application.commands.handler.FulfillNextReservationCommandHandler;
import com.turkcell.borrowservice.application.ports.BookReadModelRepository;
import com.turkcell.borrowservice.application.ports.output.BookReadModel;
import com.turkcell.borrowservice.infrastructure.messaging.inbox.InboxMessage;
import com.turkcell.borrowservice.infrastructure.messaging.inbox.InboxRepository;
import com.turkcell.common.events.BookStockChangedEvent;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

// Başka bir servisten gelen kitap stok veya durum değişikliği olayını dinler
//  ve lokal okuma modelini (BookReadModel) günceller.
@Component
public class InventoryUpdateListener {
    private final BookReadModelRepository bookReadModelRepository;
    private final InboxRepository inboxRepository;
    private final FulfillNextReservationCommandHandler fulfillNextHandler;

    public InventoryUpdateListener(BookReadModelRepository bookReadModelRepository,
                                   InboxRepository inboxRepository,
                                   FulfillNextReservationCommandHandler fulfillNextHandler) {
        this.bookReadModelRepository = bookReadModelRepository;
        this.inboxRepository = inboxRepository;
        this.fulfillNextHandler = fulfillNextHandler;
    }

    /**
     * Kitap stok/durum değişikliği olayını işlerken Inbox Pattern uygular.
     * * @param event Başka bir servisten gelen BookStockChangedEvent olayı
     */
    @Transactional // Local Read Model'i güncellediği için Transactional olmalıdır.
    public void handle(BookStockChangedEvent event) {
        try {
            // INBOX KONTROLÜ VE KAYDI
            InboxMessage inboxMessage = new InboxMessage(
                    event.getEventId(), // Event ID'si, Inbox'ın PK'sıdır
                    event.getAggregateId(),
                    event.aggregateType().toString() // BOOK
            );
            inboxRepository.saveAndFlush(inboxMessage);

            // İŞ MANTIĞI: READ MODEL GÜNCELLEME
            Optional<BookReadModel> optionalModel = bookReadModelRepository.findById(event.getAggregateId());

            if (optionalModel.isPresent()) {
                BookReadModel currentModel = optionalModel.get();

                BookReadModel updatedModel = new BookReadModel(
                        currentModel.bookId(),
                        event.newAvailableStock() > 0,
                        event.newAvailableStock(),
                        currentModel.title()
                );
                bookReadModelRepository.save(updatedModel);

            } else {
                System.err.println("WARN: BookReadModel not found for ID: " + event.getAggregateId() + ". Cannot update local inventory.");
            }

            if (event.newAvailableStock() > 0) {
                // Command'ı doğrudan çağır (aynı serviste olduğu için)
                fulfillNextHandler.handle(event.getAggregateId());
            }

        } catch (DataIntegrityViolationException e) {
            // Aynı olay ID'si zaten işlenmiş. Transaction geri alınır ve sessizce sonlanır.
            System.err.println("WARN: BookStockChangedEvent zaten işlenmiş, Read Model güncellemesi atlandı. ID: " + event.getEventId());
        } catch (Exception e) {
            throw new RuntimeException("Read Model güncellemesinde hata.", e);
        }
    }
}
