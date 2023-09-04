package com.skillup.application.order;

import com.alibaba.fastjson.JSON;
import com.skillup.application.promotion.PromotionApplication;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import com.skillup.domain.order.util.OrderStatus;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionService;
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

    @Transactional
    public OrderDomain createBuyNowOrder(OrderDomain orderDomain) {
        // 1. check promotion existing in cache by cache aside strategy
        PromotionDomain promotionDomain = promotionApplication.getById(orderDomain.getPromotionId());
        if (Objects.isNull(promotionDomain)) {
            orderDomain.setOrderStatus(OrderStatus.ITEM_ERROR);
            return orderDomain;
        }

        // 2. lock promotion stock in cache
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


}

