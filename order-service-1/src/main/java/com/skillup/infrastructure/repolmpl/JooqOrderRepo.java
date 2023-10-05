package com.skillup.infrastructure.repolmpl;

import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderRepository;
import com.skillup.domain.order.util.OrderStatus;
import com.skillup.infrastructure.jooq.tables.Orders;
import com.skillup.infrastructure.jooq.tables.records.OrdersRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JooqOrderRepo implements OrderRepository {

    @Autowired
    DSLContext dslContext;

    private static final Orders O_T = new Orders();

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createOrder(OrderDomain orderDomain) {
        dslContext.executeInsert(toRecord(orderDomain));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderDomain getOrderById(Long id) {
        // return dslContext.selectFrom(O_T).where(O_T.ORDER_NUMBER.eq(id))
        //         .fetchOptional(this::toDomain).orElse(null);

        // 加悲观锁
        return dslContext.selectFrom(O_T).where(O_T.ORDER_NUMBER.eq(id))
                .forUpdate()
                .fetchOptional(this::toDomain).orElse(null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateOrder(OrderDomain orderDomain) {
        dslContext.executeUpdate(toRecord(orderDomain));
    }



    private OrderDomain toDomain(OrdersRecord record) {
        return OrderDomain.builder()
                .orderNumber(record.getOrderNumber())
                .orderStatus(OrderStatus.CACHE.get(record.getOrderStatus()))
                .promotionId(record.getPromotionId())
                .promotionName(record.getPromotionName())
                .userId(record.getUserId())
                .orderAmount(record.getOrderAmount())
                .createTime(record.getCreateTime())
                .payTime(record.getPayTime())
                .build();
    }

    private OrdersRecord toRecord(OrderDomain domain) {
        return new OrdersRecord(
                domain.getOrderNumber(),
                domain.getOrderStatus().code,
                domain.getPromotionId(),
                domain.getPromotionName(),
                domain.getUserId(),
                domain.getOrderAmount(),
                domain.getCreateTime(),
                domain.getPayTime()
        );
    }

}
