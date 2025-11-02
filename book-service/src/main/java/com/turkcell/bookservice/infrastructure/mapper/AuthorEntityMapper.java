package com.turkcell.bookservice.infrastructure.mapper;

import com.turkcell.bookservice.domain.model.DomainId;
import com.turkcell.bookservice.domain.model.author.Author;
import com.turkcell.bookservice.infrastructure.entity.AuthorEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthorEntityMapper {
    public AuthorEntity toEntity(Author author){
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setId(author.id().value());
        authorEntity.setFullName(author.fullName());
        return authorEntity;
    }

    public Author toDomain(AuthorEntity entity) {
        return Author.rehydrate(new DomainId<Author>(entity.id()), entity.fullName());
    }
}

