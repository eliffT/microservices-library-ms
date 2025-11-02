package com.turkcell.bookservice.application.publisher.query;

import com.turkcell.bookservice.application.publisher.dto.PublisherResponse;
import com.turkcell.bookservice.application.publisher.mapper.PublisherResponseMapper;
import com.turkcell.bookservice.core.cqrs.QueryHandler;
import com.turkcell.bookservice.domain.repository.PublisherRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListPublishersQueryHandler implements QueryHandler<ListPublishersQuery, List<PublisherResponse>> {

    private final PublisherRepository publisherRepository;
    private final PublisherResponseMapper publisherResponseMapper;

    public ListPublishersQueryHandler(PublisherRepository publisherRepository, PublisherResponseMapper publisherResponseMapper) {
        this.publisherRepository = publisherRepository;
        this.publisherResponseMapper = publisherResponseMapper;
    }

    @Override
    public List<PublisherResponse> handle(ListPublishersQuery query) {
        return publisherRepository
                .findAllPaged(query.pageIndex(), query.pageSize())
                .stream()
                .map(publisherResponseMapper::toResponse)
                .toList();
    }
}

