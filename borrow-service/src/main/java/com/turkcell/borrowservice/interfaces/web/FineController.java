package com.turkcell.borrowservice.interfaces.web;

import com.turkcell.borrowservice.application.commands.PayFineCommand;
import com.turkcell.borrowservice.application.queries.handler.ListUserFinesQueryHandler;
import com.turkcell.borrowservice.application.commands.handler.PayFineCommandHandler;
import com.turkcell.borrowservice.application.queries.ListUserFinesQuery;
import com.turkcell.borrowservice.application.queries.dtos.FineDetailsDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fines")
public class FineController {

    private final PayFineCommandHandler payHandler;
    private final ListUserFinesQueryHandler listHandler; // Yeni

    public FineController(PayFineCommandHandler payHandler, ListUserFinesQueryHandler listHandler) {
        this.payHandler = payHandler;
        this.listHandler = listHandler;
    }

    @PostMapping("/{fineId}/pay")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void payFine(@PathVariable UUID fineId) {
        PayFineCommand command = new PayFineCommand(fineId);
        payHandler.handle(command);
    }

    @GetMapping
    public List<FineDetailsDto> listUserFines(
            @RequestParam UUID userId,
            @RequestParam(required = false) Boolean isPaidFilter
    ) {
        ListUserFinesQuery query = new ListUserFinesQuery(userId, isPaidFilter);
        return listHandler.handle(query);
    }
}
