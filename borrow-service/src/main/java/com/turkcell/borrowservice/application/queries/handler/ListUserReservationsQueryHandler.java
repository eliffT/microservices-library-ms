package com.turkcell.borrowservice.application.queries.handler;


import com.turkcell.borrowservice.application.mapper.ReservationAppMapper;
import com.turkcell.borrowservice.application.queries.ListUserReservationsQuery;
import com.turkcell.borrowservice.application.queries.dtos.ReservationDetailsDto;
import com.turkcell.borrowservice.domain.model.Reservation;
import com.turkcell.borrowservice.domain.repository.ReservationRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListUserReservationsQueryHandler {
    private final ReservationRepository reservationRepository;
    private final ReservationAppMapper mapper;

    public ListUserReservationsQueryHandler(ReservationRepository reservationRepository,
                                            ReservationAppMapper mapper) {
        this.reservationRepository = reservationRepository;
        this.mapper = mapper;
    }

    public List<ReservationDetailsDto> handle(ListUserReservationsQuery query) {

        List<Reservation> reservations;

        if (query.statusFilter() != null) {
            reservations = reservationRepository.findByUserIdAndStatus(query.userId(), query.statusFilter());
        } else {
            reservations = reservationRepository.findByUserId(query.userId());}

        if (reservations.isEmpty())
            return Collections.emptyList();

        return reservations.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

}
