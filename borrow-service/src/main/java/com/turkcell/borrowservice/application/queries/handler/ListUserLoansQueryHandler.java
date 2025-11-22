package com.turkcell.borrowservice.application.queries.handler;

import com.turkcell.borrowservice.application.mapper.LoanAppMapper;
import com.turkcell.borrowservice.application.queries.ListUserLoansQuery;
import com.turkcell.borrowservice.application.queries.dtos.LoanDetailsDto;
import com.turkcell.borrowservice.domain.model.Loan;
import com.turkcell.borrowservice.domain.repository.LoanRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListUserLoansQueryHandler {
    private final LoanRepository loanRepository;
    private final LoanAppMapper mapper;

    public ListUserLoansQueryHandler(LoanRepository loanRepository,
                                     LoanAppMapper mapper) {
        this.loanRepository = loanRepository;
        this.mapper = mapper;
    }
    public List<LoanDetailsDto> handle(ListUserLoansQuery query) {

        List<Loan> loans;
        if (query.statusFilter() != null) {
            loans = loanRepository.findByUserIdAndStatusIn(query.userId(), List.of(query.statusFilter()));
        } else {
            loans = loanRepository.findByUserId(query.userId());
        }

        if (loans.isEmpty())
            return Collections.emptyList();

        return loans.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


}
