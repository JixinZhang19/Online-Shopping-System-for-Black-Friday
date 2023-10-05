/*
 * This file is generated by jOOQ.
 */
package com.skillup.infrastructure.jooq.tables;


import com.skillup.infrastructure.jooq.Indexes;
import com.skillup.infrastructure.jooq.Keys;
import com.skillup.infrastructure.jooq.PromotionService;
import com.skillup.infrastructure.jooq.tables.records.PromotionLogRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
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
public class PromotionLog extends TableImpl<PromotionLogRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>promotion-service.promotion_log</code>
     */
    public static final PromotionLog PROMOTION_LOG = new PromotionLog();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PromotionLogRecord> getRecordType() {
        return PromotionLogRecord.class;
    }

    /**
     * The column <code>promotion-service.promotion_log.order_number</code>.
     */
    public final TableField<PromotionLogRecord, Long> ORDER_NUMBER = createField(DSL.name("order_number"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>promotion-service.promotion_log.user_id</code>.
     */
    public final TableField<PromotionLogRecord, String> USER_ID = createField(DSL.name("user_id"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>promotion-service.promotion_log.promotion_id</code>.
     */
    public final TableField<PromotionLogRecord, String> PROMOTION_ID = createField(DSL.name("promotion_id"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>promotion-service.promotion_log.operation_name</code>.
     */
    public final TableField<PromotionLogRecord, String> OPERATION_NAME = createField(DSL.name("operation_name"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>promotion-service.promotion_log.create_time</code>.
     */
    public final TableField<PromotionLogRecord, LocalDateTime> CREATE_TIME = createField(DSL.name("create_time"), SQLDataType.LOCALDATETIME(0).nullable(false), this, "");

    private PromotionLog(Name alias, Table<PromotionLogRecord> aliased) {
        this(alias, aliased, null);
    }

    private PromotionLog(Name alias, Table<PromotionLogRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>promotion-service.promotion_log</code> table reference
     */
    public PromotionLog(String alias) {
        this(DSL.name(alias), PROMOTION_LOG);
    }

    /**
     * Create an aliased <code>promotion-service.promotion_log</code> table reference
     */
    public PromotionLog(Name alias) {
        this(alias, PROMOTION_LOG);
    }

    /**
     * Create a <code>promotion-service.promotion_log</code> table reference
     */
    public PromotionLog() {
        this(DSL.name("promotion_log"), null);
    }

    public <O extends Record> PromotionLog(Table<O> child, ForeignKey<O, PromotionLogRecord> key) {
        super(child, key, PROMOTION_LOG);
    }

    @Override
    public Schema getSchema() {
        return PromotionService.PROMOTION_SERVICE;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PROMOTION_LOG_IDX_ORDER_NUMBER, Indexes.PROMOTION_LOG_IDX_USER_ID);
    }

    @Override
    public UniqueKey<PromotionLogRecord> getPrimaryKey() {
        return Keys.KEY_PROMOTION_LOG_PRIMARY;
    }

    @Override
    public List<UniqueKey<PromotionLogRecord>> getKeys() {
        return Arrays.<UniqueKey<PromotionLogRecord>>asList(Keys.KEY_PROMOTION_LOG_PRIMARY);
    }

    @Override
    public PromotionLog as(String alias) {
        return new PromotionLog(DSL.name(alias), this);
    }

    @Override
    public PromotionLog as(Name alias) {
        return new PromotionLog(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PromotionLog rename(String name) {
        return new PromotionLog(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PromotionLog rename(Name name) {
        return new PromotionLog(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, String, String, String, LocalDateTime> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
