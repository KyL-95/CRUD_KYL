package com.vti.testing.bosung.eventlistener;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class TromCho extends ApplicationEvent {
    public TromCho(Object source) {
        super(source);
    }
}
