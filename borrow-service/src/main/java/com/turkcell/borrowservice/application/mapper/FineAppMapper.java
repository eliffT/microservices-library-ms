package com.turkcell.borrowservice.application.mapper;

import com.turkcell.borrowservice.application.queries.dtos.FineDetailsDto;
import com.turkcell.borrowservice.domain.model.Fine;
import org.springframework.stereotype.Component;

@Component
public class FineAppMapper {

    public FineDetailsDto toDto(Fine fine) {
        return new FineDetailsDto(
                fine.id().value(),
                fine.loanId(),
                fine.amount(),
                fine.paymentDate(),
                fine.isPaid()
        );
    }
}

