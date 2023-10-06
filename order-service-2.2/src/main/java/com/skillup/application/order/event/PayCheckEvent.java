package com.skillup.application.order.event;

import com.skillup.domain.order.OrderDomain;
import org.springframework.context.ApplicationEvent;

public class PayCheckEvent extends ApplicationEvent {

    public OrderDomain orderDomain;

    public PayCheckEvent(Object source, OrderDomain orderDomain) {
        super(source);
        this.orderDomain = orderDomain;
    }
}
