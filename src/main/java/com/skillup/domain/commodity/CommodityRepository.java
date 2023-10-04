package com.skillup.domain.commodity;

public interface CommodityRepository {
    void createCommodity(CommodityDomain commodityDomain);

    CommodityDomain readCommodityById(String commodityId);

    CommodityDomain readCommodityByName(String commodityName);

    void updateCommodity(CommodityDomain commodityDomain);
}
