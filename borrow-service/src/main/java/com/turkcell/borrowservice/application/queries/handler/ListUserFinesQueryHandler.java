package com.turkcell.borrowservice.application.queries.handler;

import com.turkcell.borrowservice.application.mapper.FineAppMapper;
import com.turkcell.borrowservice.application.queries.ListUserFinesQuery;
import com.turkcell.borrowservice.application.queries.dtos.FineDetailsDto;
import com.turkcell.borrowservice.domain.model.Fine;
import com.turkcell.borrowservice.domain.repository.FineRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListUserFinesQueryHandler {
    private final FineRepository fineRepository;
    private final FineAppMapper mapper;

    public ListUserFinesQueryHandler(FineRepository fineRepository,
                                     FineAppMapper mapper) {
        this.fineRepository = fineRepository;
        this.mapper = mapper;
    }

    public List<FineDetailsDto> handle(ListUserFinesQuery query) {
        List<Fine> fines;

        if (query.isPaidFilter() != null) {
            fines = fineRepository.findByUserIdAndIsPaid(query.userId(), query.isPaidFilter());
        } else {
            fines = fineRepository.findByUserId(query.userId());
        }

        if (fines.isEmpty())
            return Collections.emptyList();

        return fines.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


}
