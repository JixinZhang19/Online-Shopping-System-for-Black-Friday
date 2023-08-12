package com.skillup.domain.order.util;

import java.util.HashMap;

public enum OrderStatus {

    // 订单状态
    // -2:promotion invalid, -1:库存不足订单, 0:预订单, 1:已创建等待付款, 2:已付款, 3:订单过期或者无效
    ITEM_ERROR(-2),
    OUT_OF_STOCK(-1),
    READY(0),
    CREATED(1),
    PAYED(2),
    OVERTIME(3);

    public Integer code;

    OrderStatus(Integer code) {
        this.code = code;
    }

    // 通过code获得OrderStatus
    // record to domain 时需要
    public static HashMap<Integer, OrderStatus> CACHE = new HashMap<Integer, OrderStatus>(){{
        put(-2, ITEM_ERROR);
        put(-1, OUT_OF_STOCK);
        put(0, READY);
        put(1, CREATED);
        put(2, PAYED);
        put(3, OVERTIME);
    }};
}
