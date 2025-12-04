package com.turkcell.borrowservice.infrastructure.mapper;

import com.turkcell.borrowservice.application.ports.output.BookReadModel;
import com.turkcell.borrowservice.infrastructure.persistence.entity.readmodel.BookReadModelEntity;
import org.springframework.stereotype.Component;

@Component
public class BookModelEntityMapper {
    public BookReadModelEntity toEntity(BookReadModel bookReadModel) {

        BookReadModelEntity entity = new BookReadModelEntity();
        entity.setBookId(bookReadModel.bookId());
        entity.setAvailable(bookReadModel.isAvailable());
        entity.setAvailableStock(bookReadModel.availableStock());
        return entity;
    }

    public  BookReadModel toDomain(BookReadModelEntity bookReadModelEntity) {

        return new BookReadModel(
                bookReadModelEntity.bookId(),
                bookReadModelEntity.isAvailable(),
                bookReadModelEntity.availableStock(),
                bookReadModelEntity.title());

    }
}
