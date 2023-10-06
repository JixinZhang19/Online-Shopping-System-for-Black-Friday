package com.skillup.application.order.handler;

import com.alibaba.fastjson2.JSON;
import com.skillup.application.order.MqSendRepository;
import com.skillup.application.order.event.PayCheckEvent;
import com.skillup.application.promotionFeignClient.PromotionServiceApi;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import com.skillup.domain.order.util.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class PayCheckEventHandler implements ApplicationListener<PayCheckEvent> {

    @Autowired
    PromotionServiceApi promotionServiceApi;

    @Autowired
    OrderService orderService;

    // @Autowired
    // StockCacheService stockCacheService;

    @Autowired
    MqSendRepository mqSendRepository;

    @Value("${promotion.topic.revert-stock}")
    String revertStockTopic;


    @Override
    public void onApplicationEvent(PayCheckEvent event) {

        OrderDomain orderDomain = event.orderDomain;

        // 1. get newest orderDomain from database
        OrderDomain currentOrderDomain = orderService.getOrderById(orderDomain.getOrderNumber());
        if (Objects.isNull(currentOrderDomain)) {
            throw new RuntimeException("Order does not exist");
        }

        // ※ keep idempotent: not necessary
        // 2. if unpaid: order status = 1 (created)
        if (currentOrderDomain.getOrderStatus().equals(OrderStatus.CREATED)) {
            // 2.1 set order status = 3 (overtime)
            currentOrderDomain.setOrderStatus(OrderStatus.OVERTIME);
            orderService.updateOrder(currentOrderDomain);
            // 2.2 revert stock in redis
            // StockCacheDomain stockCacheDomain = StockCacheDomain.builder().promotionId(currentOrderDomain.getPromotionId()).build();
            // stockCacheService.revertStock(stockCacheDomain);
            ResponseEntity<Boolean> booleanResponseEntity = promotionServiceApi.revertStock(currentOrderDomain.getPromotionId(), currentOrderDomain.getOrderNumber());
            if (booleanResponseEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST) || !booleanResponseEntity.hasBody() || Objects.isNull(booleanResponseEntity.getBody())) {
                log.error("Promotion service revert stock failed");
                // 介入人工校验（数据一致性）
                return;
            }
            boolean isReverted = booleanResponseEntity.getBody();
            if (!isReverted) {
                log.error("Promotion service revert stock failed: isReverted = false");
                // 介入人工校验（数据一致性）
                return;
            }
            // 2.3 send revert-stock to MySQL message to MQ
            mqSendRepository.sendMessageToTopic(revertStockTopic, JSON.toJSONString(orderDomain));
        }
        // 3. if paid: order status = 2 (paid)
        // meaning that deduct stock in database already executed after payment
        else if (currentOrderDomain.getOrderStatus().equals(OrderStatus.PAYED)) {
            log.info("OrderId: "+ currentOrderDomain.getOrderNumber() + " is already paid");
        }
        // 4. if already overtime/canceled
        else if (currentOrderDomain.getOrderStatus().equals(OrderStatus.OVERTIME)) {
            log.info("OrderId: "+ currentOrderDomain.getOrderNumber() + " is already overtime/canceled");
        }
    }
}
