package com.turkcell.bookservice.infrastructure.persistence.adapter;

import com.turkcell.bookservice.domain.model.DomainId;
import com.turkcell.bookservice.domain.model.publisher.Publisher;
import com.turkcell.bookservice.domain.repository.PublisherRepository;
import com.turkcell.bookservice.infrastructure.persistence.entity.PublisherEntity;
import com.turkcell.bookservice.infrastructure.persistence.jparepository.PublisherJpaRepository;
import com.turkcell.bookservice.infrastructure.mapper.PublisherEntityMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PublisherRepositoryAdapter implements PublisherRepository {
    private final PublisherJpaRepository repository;
    private final PublisherEntityMapper mapper;

    public PublisherRepositoryAdapter(PublisherJpaRepository repository, PublisherEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Publisher save(Publisher publisher) {
        PublisherEntity publisherEntity = mapper.toEntity(publisher);
        publisherEntity = repository.save(publisherEntity);
        return mapper.toDomain(publisherEntity);
    }

    @Override
    public Optional<Publisher> findById(DomainId<Publisher> publisherId) {
        return repository.findById(publisherId.value()).map(mapper::toDomain);
    }

    @Override
    public List<Publisher> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Publisher> findAllPaged(Integer pageIndex, Integer pageSize) {
        return repository.findAll(PageRequest.of(pageIndex,pageSize))
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void delete(DomainId<Publisher> publisherId) {
        repository.deleteById(publisherId.value());
    }
}
