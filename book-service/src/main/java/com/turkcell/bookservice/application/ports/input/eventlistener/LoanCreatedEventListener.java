package com.turkcell.bookservice.application.ports.input.eventlistener;

import com.turkcell.common.events.LoanCreatedEvent;

public interface LoanCreatedEventListener {
    void handle(LoanCreatedEvent event);
}
