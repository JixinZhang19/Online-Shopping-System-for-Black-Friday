/*
 * This file is generated by jOOQ.
 */
package com.skillup.infrastructure.jooq;


import com.skillup.infrastructure.jooq.tables.Orders;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OrderService_1 extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>order-service-1</code>
     */
    public static final OrderService_1 ORDER_SERVICE_1 = new OrderService_1();

    /**
     * The table <code>order-service-1.orders</code>.
     */
    public final Orders ORDERS = Orders.ORDERS;

    /**
     * No further instances allowed
     */
    private OrderService_1() {
        super("order-service-1", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
            Orders.ORDERS);
    }
}
