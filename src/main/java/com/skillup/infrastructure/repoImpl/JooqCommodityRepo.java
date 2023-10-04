package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.commodity.CommodityDomain;
import com.skillup.domain.commodity.CommodityRepository;
import com.skillup.infrastructure.jooq.tables.Commodity;
import com.skillup.infrastructure.jooq.tables.records.CommodityRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JooqCommodityRepo  implements CommodityRepository {

    @Autowired
    DSLContext dslContext;

    public static final Commodity COMMODITY_T = new Commodity();

    @Override
    public void createCommodity(CommodityDomain commodityDomain) {
        dslContext.executeInsert(toRecord(commodityDomain));
    }

    @Override
    public CommodityDomain readCommodityById(String commodityId) {
        Optional<CommodityDomain> commodityDomainOptimal = dslContext.selectFrom(COMMODITY_T).where(COMMODITY_T.COMMODITY_ID.eq(commodityId)).fetchOptional(this::toDomain);
        return commodityDomainOptimal.orElse(null);
    }

    @Override
    public CommodityDomain readCommodityByName(String commodityName) {
        Optional<CommodityDomain> commodityDomainOptional = dslContext.selectFrom(COMMODITY_T).where(COMMODITY_T.COMMODITY_NAME.eq(commodityName)).fetchOptional(this::toDomain);
        return commodityDomainOptional.orElse(null);
    }

    @Override
    public void updateCommodity(CommodityDomain commodityDomain) {
        dslContext.executeUpdate(toRecord(commodityDomain));
    }

    private CommodityRecord toRecord(CommodityDomain commodityDomain){
        CommodityRecord commodityRecord = new CommodityRecord();

        commodityRecord.setCommodityId(commodityDomain.getCommodityId());
        commodityRecord.setCommodityName(commodityDomain.getCommodityName());
        commodityRecord.setDescription(commodityDomain.getDescription());
        commodityRecord.setPrice(commodityDomain.getPrice());
        commodityRecord.setImageUrl(commodityDomain.getImageUrl());

        return commodityRecord;
    }

    private CommodityDomain toDomain(CommodityRecord commodityRecord){

        return CommodityDomain.builder()
                .commodityId(commodityRecord.getCommodityId())
                .commodityName(commodityRecord.getCommodityName())
                .price(commodityRecord.getPrice())
                .description(commodityRecord.getDescription())
                .imageUrl(commodityRecord.getImageUrl())
                .build();
    }



}


