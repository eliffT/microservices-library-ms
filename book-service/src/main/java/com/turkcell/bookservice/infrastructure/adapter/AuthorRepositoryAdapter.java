package com.turkcell.bookservice.infrastructure.adapter;

import com.turkcell.bookservice.domain.model.DomainId;
import com.turkcell.bookservice.domain.model.author.Author;
import com.turkcell.bookservice.domain.repository.AuthorRepository;
import com.turkcell.bookservice.infrastructure.entity.AuthorEntity;
import com.turkcell.bookservice.infrastructure.jparepository.AuthorJpaRepository;
import com.turkcell.bookservice.infrastructure.mapper.AuthorEntityMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryAdapter implements AuthorRepository {

    private final AuthorJpaRepository authorJpaRepository;
    private final AuthorEntityMapper authorEntityMapper;

    public AuthorRepositoryAdapter(AuthorJpaRepository authorJpaRepository, AuthorEntityMapper authorEntityMapper) {
        this.authorJpaRepository = authorJpaRepository;
        this.authorEntityMapper = authorEntityMapper;
    }

    @Override
    public Author save(Author author) {
        AuthorEntity authorEntity = authorEntityMapper.toEntity(author);
        authorEntity = authorJpaRepository.save(authorEntity);
        return authorEntityMapper.toDomain(authorEntity);
    }

    @Override
    public Optional<Author> findById(DomainId<Author> authorId) {
        return authorJpaRepository.findById(authorId.value()).map(authorEntityMapper::toDomain);
    }

    @Override
    public List<Author> findAll() {
        return authorJpaRepository
                .findAll()
                .stream()
                .map(authorEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Author> findAllPaged(Integer pageIndex, Integer pageSize) {
        return authorJpaRepository
                .findAll(PageRequest.of(pageIndex, pageSize))
                .stream()
                .map(authorEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(DomainId<Author> authorId) {
        authorJpaRepository.deleteById(authorId.value());
    }
}