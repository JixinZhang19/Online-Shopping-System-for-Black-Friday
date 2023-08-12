package com.skillup.domain.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public OrderDomain createOrder(OrderDomain orderDomain) {
        orderRepository.createOrder(orderDomain);
        return orderDomain;
    }

    public OrderDomain getOrderById(Long id) {
        return orderRepository.getOrderById(id);
    }




}
