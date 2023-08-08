package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionRepository;
import com.skillup.infrastructure.jooq.tables.records.PromotionRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JooqPromotionRepo implements PromotionRepository {
    @Autowired
    DSLContext dslContext;


    @Override
    public void createPromotion(PromotionDomain domain) {
        dslContext.executeInsert(toRecord(domain));
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

}
