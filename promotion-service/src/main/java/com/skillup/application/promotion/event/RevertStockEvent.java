package com.skillup.application.promotion.event;

import com.skillup.domain.order.OrderDomain;
import org.springframework.context.ApplicationEvent;

public class RevertStockEvent extends ApplicationEvent {

    public OrderDomain orderDomain;

    public RevertStockEvent(Object source, OrderDomain orderDomain) {
        super(source);
        this.orderDomain = orderDomain;
    }
}