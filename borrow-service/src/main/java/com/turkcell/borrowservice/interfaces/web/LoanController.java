package com.turkcell.borrowservice.interfaces.web;

import com.turkcell.borrowservice.application.commands.BorrowBookCommand;
import com.turkcell.borrowservice.application.commands.ReturnBookCommand;
import com.turkcell.borrowservice.application.commands.handler.LoanCreatedCommandHandler;
import com.turkcell.borrowservice.application.queries.handler.ListUserLoansQueryHandler;
import com.turkcell.borrowservice.application.commands.handler.ReturnBookCommandHandler;
import com.turkcell.borrowservice.application.queries.ListUserLoansQuery;
import com.turkcell.borrowservice.application.queries.dtos.LoanDetailsDto;
import com.turkcell.borrowservice.domain.model.enumstatus.LoanStatus;
import com.turkcell.borrowservice.interfaces.request.BorrowBookRequest;
import com.turkcell.borrowservice.interfaces.response.IdResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {

    private final LoanCreatedCommandHandler borrowHandler;
    private final ReturnBookCommandHandler returnHandler;
    private final ListUserLoansQueryHandler listLoansQueryHandler;

    public LoanController(LoanCreatedCommandHandler borrowHandler, ReturnBookCommandHandler returnHandler, ListUserLoansQueryHandler listLoansQueryHandler) {
        this.borrowHandler = borrowHandler;
        this.returnHandler = returnHandler;
        this.listLoansQueryHandler = listLoansQueryHandler;
    }

    @PostMapping
    public ResponseEntity<IdResponse> borrowBook(@Valid @RequestBody BorrowBookRequest request) {

        // 1. Web Request'i Application Command'a dönüştürülür
        BorrowBookCommand command = new BorrowBookCommand(
                request.userId(),
                request.bookId(),
                request.loanDays()
        );
        // 2. Command Handler çağrılır
        UUID loanId = borrowHandler.handle(command);

        // 3. 201 Created ve oluşturulan ID döndürülür
        return new ResponseEntity<>(new IdResponse(loanId), HttpStatus.CREATED);
    }

    @PostMapping("/{loanId}/return")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 No Content
    public void returnBook(@PathVariable UUID loanId) {

        // Command Handler çağrılır
        ReturnBookCommand command = new ReturnBookCommand(loanId);
        returnHandler.handle(command);
    }

    @GetMapping("/users/{userId}")
    public List<LoanDetailsDto> getUserLoans(
            @PathVariable UUID userId,
            // İsteğe bağlı filtreleme
            @RequestParam(required = false) LoanStatus status
    ) {
        // Query DTO oluşturulur
        ListUserLoansQuery query = new ListUserLoansQuery(userId, status);

        // Query Handler çağrılır
        return listLoansQueryHandler.handle(query);
    }

}
