package com.skillup.application.order;

import com.alibaba.fastjson.JSON;
import com.skillup.application.promotion.PromotionApplication;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import com.skillup.domain.order.util.OrderStatus;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.stockCache.StockCacheDomain;
import com.skillup.domain.stockCache.StockCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class OrderApplication {

    @Autowired
    PromotionApplication promotionApplication;

    @Autowired
    StockCacheService stockCacheService;

    @Autowired
    OrderService orderService;

    @Autowired
    MqSendRepository mqSendRepository;

    @Value("${order.topic.create-order}")
    String createOrderTopic;

    @Value("${promotion.topic.deduct-stock}")
    String deductStockTopic;

    @Transactional
    public OrderDomain createBuyNowOrder(OrderDomain orderDomain) {

        // 1. check promotion existing in cache by cache aside strategy
        // TODO: keep idempotent when using microservice
        PromotionDomain promotionDomain = promotionApplication.getById(orderDomain.getPromotionId());
        if (Objects.isNull(promotionDomain)) {
            orderDomain.setOrderStatus(OrderStatus.ITEM_ERROR);
            return orderDomain;
        }

        // 2. lock promotion stock in cache
        // TODO: keep idempotent when using microservice
        boolean isLocked = stockCacheService.lockStock(StockCacheDomain.builder().promotionId(orderDomain.getPromotionId()).build());
        if (!isLocked) {
            orderDomain.setOrderStatus(OrderStatus.OUT_OF_STOCK);
            return orderDomain;
        }

        // 3. fulfill some order detail (create time && status)
        orderDomain.setCreateTime(LocalDateTime.now());
        orderDomain.setOrderStatus(OrderStatus.CREATED);

        // 4. send create-order message to MQ
        mqSendRepository.sendMessageToTopic(createOrderTopic, JSON.toJSONString(orderDomain));

        return orderDomain;
    }


    @Transactional
    public OrderDomain payBuyNowOrder(Long orderNumber, Integer existStatus, Integer expectStatus) {
        OrderDomain orderDomain = orderService.getOrderById(orderNumber);
        if (Objects.isNull(orderDomain)) return null;
        // invalid request for payment
        if (!existStatus.equals(OrderStatus.CREATED.code) || !expectStatus.equals(OrderStatus.PAYED.code)) {
            return orderDomain;
        }
        if (existStatus.equals((orderDomain.getOrderStatus().code))) {
            // 1. third party payment
            boolean isPayed = thirdPartyPay();
            // 2. if paid
            if (isPayed) {
                // 2.1 set order status = paid
                orderDomain.setOrderStatus(OrderStatus.PAYED);
                // orderDomain.setOrderStatus(OrderStatus.CACHE.get(existStatus));
                orderDomain.setPayTime(LocalDateTime.now());
                orderService.updateOrder(orderDomain);
                // 2.2 send deduct-stock to MySQL message to MQ
                mqSendRepository.sendMessageToTopic(deductStockTopic, JSON.toJSONString(orderDomain));
            }
            // TODO: 3. if not paid or paid error...
        }
        return orderDomain;
    }

    public boolean thirdPartyPay() {
        return true;
    }
}

