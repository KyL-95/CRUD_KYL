package com.vti.testing.bosung.eventlistener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class NhaHangXom {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    public void tromCho(){
        System.out.println("Có thằng trộm chó, bắt lấy nó");
        applicationEventPublisher.publishEvent(new TromCho(this));
    }

}
