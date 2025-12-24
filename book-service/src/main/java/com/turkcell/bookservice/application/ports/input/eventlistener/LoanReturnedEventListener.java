package com.turkcell.bookservice.application.ports.input.eventlistener;

import com.turkcell.common.events.LoanReturnedEvent;

// Port: Altyapıyı iş mantığından soyutlar

public interface LoanReturnedEventListener {
    void handle(LoanReturnedEvent event);
}