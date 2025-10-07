package com.turkcell.reservation_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/reservations")
public class ReservationController {

    @GetMapping
    public String getReservations(){
        System.out.println("get reservations");
        return "Reservation Controller";
    }
}
