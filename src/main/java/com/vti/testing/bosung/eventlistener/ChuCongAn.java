package com.vti.testing.bosung.eventlistener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ChuCongAn {

    @EventListener(TromCho.class)
    public void batTromCho(){
        System.out.println("Chịu, nhà chú công an cũng vừa mất chó");
    }
}
