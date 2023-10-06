/*
 * This file is generated by jOOQ.
 */
package com.skillup.infrastructure.jooq;


import com.skillup.infrastructure.jooq.tables.Orders;
import com.skillup.infrastructure.jooq.tables.records.OrdersRecord;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in 
 * order-service-1.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<OrdersRecord> KEY_ORDERS_PRIMARY = Internal.createUniqueKey(Orders.ORDERS, DSL.name("KEY_orders_PRIMARY"), new TableField[] { Orders.ORDERS.ORDER_NUMBER }, true);
}
