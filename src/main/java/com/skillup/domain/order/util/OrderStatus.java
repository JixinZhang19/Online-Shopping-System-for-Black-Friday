package com.skillup.domain.order.util;

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

}
