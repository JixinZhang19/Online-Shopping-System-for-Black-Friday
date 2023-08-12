package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionRepository;
import com.skillup.domain.promotion.stockStrategy.StockOperation;
import com.skillup.infrastructure.jooq.tables.Promotion;
import com.skillup.infrastructure.jooq.tables.records.PromotionRecord;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository(value = "optimistic")
@Slf4j
public class JooqPromotionRepo implements PromotionRepository, StockOperation {
    @Autowired
    DSLContext dslContext;

    private static final Promotion P_T = new Promotion();

    @Override
    public void createPromotion(PromotionDomain domain) {
        dslContext.executeInsert(toRecord(domain));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PromotionDomain getById(String id) {
        return dslContext.selectFrom(P_T).where(P_T.PROMOTION_ID.eq(id)).fetchOptional(this::toDomain).orElse(null);
    }

    @Override
    public List<PromotionDomain> getByStatus(Integer status) {
        return dslContext.selectFrom(P_T).where(P_T.STATUS.eq(status)).fetch(this::toDomain); // 没有则 list 为空
    }

    @Override
    public void updatePromotion(PromotionDomain promotionDomain) {
        dslContext.executeUpdate(toRecord(promotionDomain));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean lockStock(String id) {
        // Database level lock stock:
        // update promotion
        // set AVAILABLE_STOCK-=1, LOCK_STOCK+=1 where PROMOTION_ID=id and AVAILABLE_STOCK>0
        log.info("----------- Optimistic Locking Strategy: lockStock -----------");
        int isLocked = dslContext.update(P_T)
                                 .set(P_T.AVAILABLE_STOCK, P_T.AVAILABLE_STOCK.subtract(1))
                                 .set(P_T.LOCK_STOCK, P_T.LOCK_STOCK.add(1))
                                 .where(P_T.PROMOTION_ID.eq(id).and(P_T.AVAILABLE_STOCK.greaterThan(0L)))
                                 .execute();
        return isLocked == 1;
    }

    @Override
    public boolean revertStock(String id) {
        // Database level revert stock:
        // update promotion
        // set AVAILABLE_STOCK+=1, LOCK_STOCK-=1 where PROMOTION_ID=id and LOCK_STOCK>0
        log.info("----------- Optimistic Locking Strategy: revertStock -----------");
        int isReverted = dslContext.update(P_T)
                .set(P_T.AVAILABLE_STOCK, P_T.AVAILABLE_STOCK.add(1))
                .set(P_T.LOCK_STOCK, P_T.LOCK_STOCK.subtract(1))
                .where(P_T.PROMOTION_ID.eq(id).and(P_T.LOCK_STOCK.greaterThan(0L)))
                .execute();
        return isReverted == 1;
    }

    @Override
    public boolean deductStock(String id) {
        // Database deduct revert stock:
        // update promotion
        // set LOCK_STOCK-=1 where PROMOTION_ID=id and LOCK_STOCK>0
        log.info("----------- Optimistic Locking Strategy: deductStock -----------");
        int isDeducted = dslContext.update(P_T)
                .set(P_T.LOCK_STOCK, P_T.LOCK_STOCK.subtract(1))
                .where(P_T.PROMOTION_ID.eq(id).and(P_T.LOCK_STOCK.greaterThan(0L)))
                .execute();
        return isDeducted == 1;
    }

    private PromotionRecord toRecord(PromotionDomain domain) {
        PromotionRecord promotionRecord = new PromotionRecord();
        promotionRecord.setPromotionId(domain.getPromotionId());
        promotionRecord.setPromotionName(domain.getPromotionName());
        promotionRecord.setCommodityId(domain.getCommodityId());
        promotionRecord.setOriginalPrice(domain.getOriginalPrice());
        promotionRecord.setPromotionPrice(domain.getPromotionalPrice());
        promotionRecord.setStartTime(domain.getStartTime());
        promotionRecord.setEndTime(domain.getEndTime());
        promotionRecord.setStatus(domain.getStatus());
        promotionRecord.setTotalStock(domain.getTotalStock());
        promotionRecord.setAvailableStock(domain.getAvailableStock());
        promotionRecord.setLockStock(domain.getLockStock());
        promotionRecord.setImageUrl(domain.getImageUrl());
        return promotionRecord;
    }

    private PromotionDomain toDomain(PromotionRecord record) {
        return PromotionDomain
                .builder()
                .promotionId(record.getPromotionId())
                .promotionName(record.getPromotionName())
                .commodityId(record.getCommodityId())
                .originalPrice(record.getOriginalPrice())
                .promotionalPrice(record.getPromotionPrice())
                .status(record.getStatus())
                .startTime(record.getStartTime())
                .endTime(record.getEndTime())
                .imageUrl(record.getImageUrl())
                .totalStock(record.getTotalStock())
                .availableStock(record.getAvailableStock())
                .lockStock(record.getLockStock())
                .build();
    }

}
