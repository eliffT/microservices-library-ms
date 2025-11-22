package com.turkcell.borrowservice.interfaces.web;

import com.turkcell.borrowservice.application.commands.CancelReservationCommand;
import com.turkcell.borrowservice.application.commands.CreateReservationCommand;
import com.turkcell.borrowservice.application.queries.handler.ListUserReservationsQueryHandler;
import com.turkcell.borrowservice.application.commands.handler.CancelReservationCommandHandler;
import com.turkcell.borrowservice.application.commands.handler.CreateReservationCommandHandler;
import com.turkcell.borrowservice.application.queries.ListUserReservationsQuery;
import com.turkcell.borrowservice.application.queries.dtos.ReservationDetailsDto;
import com.turkcell.borrowservice.domain.model.enumstatus.ReservationStatus;
import com.turkcell.borrowservice.interfaces.request.CreateReservationRequest;
import com.turkcell.borrowservice.interfaces.response.IdResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    private final CreateReservationCommandHandler createHandler;
    private final CancelReservationCommandHandler cancelHandler;
    private final ListUserReservationsQueryHandler listHandler;

    public ReservationController(CreateReservationCommandHandler createHandler,
                                 CancelReservationCommandHandler cancelHandler,
                                 ListUserReservationsQueryHandler listHandler) {
        this.createHandler = createHandler;
        this.cancelHandler = cancelHandler;
        this.listHandler = listHandler;
    }


    @PostMapping
    public ResponseEntity<IdResponse> createReservation(@Valid @RequestBody CreateReservationRequest request) {
        // CreateReservationRequest, Command DTO'sundan farklı olarak API'ye özel alanlar içerebilir.
        CreateReservationCommand command = new CreateReservationCommand(request.userId(), request.bookId());
        UUID reservationId = createHandler.handle(command);
        return new ResponseEntity<>(new IdResponse(reservationId), HttpStatus.CREATED);
    }

    @PostMapping("/{reservationId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelReservation(@PathVariable UUID reservationId) {
        CancelReservationCommand command = new CancelReservationCommand(reservationId);
        cancelHandler.handle(command);
    }

    @GetMapping
    public List<ReservationDetailsDto> listUserReservations(
            @RequestParam UUID userId,
            @RequestParam(required = false) ReservationStatus statusFilter
    ) {
        ListUserReservationsQuery query = new ListUserReservationsQuery(userId, statusFilter);
        return listHandler.handle(query);
    }
}
