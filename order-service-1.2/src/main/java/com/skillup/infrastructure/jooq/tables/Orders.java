/*
 * This file is generated by jOOQ.
 */
package com.skillup.infrastructure.jooq.tables;


import com.skillup.infrastructure.jooq.Indexes;
import com.skillup.infrastructure.jooq.Keys;
import com.skillup.infrastructure.jooq.OrderService_1;
import com.skillup.infrastructure.jooq.tables.records.OrdersRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row8;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Orders extends TableImpl<OrdersRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>order-service-1.orders</code>
     */
    public static final Orders ORDERS = new Orders();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<OrdersRecord> getRecordType() {
        return OrdersRecord.class;
    }

    /**
     * The column <code>order-service-1.orders.order_number</code>.
     */
    public final TableField<OrdersRecord, Long> ORDER_NUMBER = createField(DSL.name("order_number"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>order-service-1.orders.order_status</code>.
     */
    public final TableField<OrdersRecord, Integer> ORDER_STATUS = createField(DSL.name("order_status"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>order-service-1.orders.promotion_id</code>.
     */
    public final TableField<OrdersRecord, String> PROMOTION_ID = createField(DSL.name("promotion_id"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>order-service-1.orders.promotion_name</code>.
     */
    public final TableField<OrdersRecord, String> PROMOTION_NAME = createField(DSL.name("promotion_name"), SQLDataType.VARCHAR(128).nullable(false), this, "");

    /**
     * The column <code>order-service-1.orders.user_id</code>.
     */
    public final TableField<OrdersRecord, String> USER_ID = createField(DSL.name("user_id"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>order-service-1.orders.order_amount</code>.
     */
    public final TableField<OrdersRecord, Integer> ORDER_AMOUNT = createField(DSL.name("order_amount"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>order-service-1.orders.create_time</code>.
     */
    public final TableField<OrdersRecord, LocalDateTime> CREATE_TIME = createField(DSL.name("create_time"), SQLDataType.LOCALDATETIME(0).nullable(false), this, "");

    /**
     * The column <code>order-service-1.orders.pay_time</code>.
     */
    public final TableField<OrdersRecord, LocalDateTime> PAY_TIME = createField(DSL.name("pay_time"), SQLDataType.LOCALDATETIME(0), this, "");

    private Orders(Name alias, Table<OrdersRecord> aliased) {
        this(alias, aliased, null);
    }

    private Orders(Name alias, Table<OrdersRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>order-service-1.orders</code> table reference
     */
    public Orders(String alias) {
        this(DSL.name(alias), ORDERS);
    }

    /**
     * Create an aliased <code>order-service-1.orders</code> table reference
     */
    public Orders(Name alias) {
        this(alias, ORDERS);
    }

    /**
     * Create a <code>order-service-1.orders</code> table reference
     */
    public Orders() {
        this(DSL.name("orders"), null);
    }

    public <O extends Record> Orders(Table<O> child, ForeignKey<O, OrdersRecord> key) {
        super(child, key, ORDERS);
    }

    @Override
    public Schema getSchema() {
        return OrderService_1.ORDER_SERVICE_1;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.ORDERS_IDX_USER_ID);
    }

    @Override
    public UniqueKey<OrdersRecord> getPrimaryKey() {
        return Keys.KEY_ORDERS_PRIMARY;
    }

    @Override
    public List<UniqueKey<OrdersRecord>> getKeys() {
        return Arrays.<UniqueKey<OrdersRecord>>asList(Keys.KEY_ORDERS_PRIMARY);
    }

    @Override
    public Orders as(String alias) {
        return new Orders(DSL.name(alias), this);
    }

    @Override
    public Orders as(Name alias) {
        return new Orders(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Orders rename(String name) {
        return new Orders(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Orders rename(Name name) {
        return new Orders(name, null);
    }

    // -------------------------------------------------------------------------
    // Row8 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row8<Long, Integer, String, String, String, Integer, LocalDateTime, LocalDateTime> fieldsRow() {
        return (Row8) super.fieldsRow();
    }
}
