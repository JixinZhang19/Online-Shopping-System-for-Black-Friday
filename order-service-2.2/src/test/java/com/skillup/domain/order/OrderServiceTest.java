package com.skillup.domain.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Test
    void getOrderInfo() {
        OrderDomain returnOrder = OrderDomain.builder().orderNumber(1L).build();

        when(orderRepository.getOrderById(1L)).thenReturn(returnOrder);
        when(orderRepository.getOrderById(0L)).thenReturn(null);


        OrderDomain order0 = orderService.getOrderById(0L);
        assertThat(order0).isEqualTo(null);

        OrderDomain order1 = orderService.getOrderById(1L);
        assertThat(order1).isEqualTo(returnOrder);
    }
}