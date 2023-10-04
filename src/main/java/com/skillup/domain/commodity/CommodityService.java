package com.skillup.domain.commodity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommodityService {
    @Autowired
    CommodityRepository commodityRepository;

    public CommodityDomain createCommodity(CommodityDomain commodityDomain) {
        commodityRepository.createCommodity(commodityDomain);
        return commodityDomain;
    }

    public CommodityDomain readCommodityById(String commodityId) {
        return commodityRepository.readCommodityById(commodityId);
    }

    public CommodityDomain readCommodityByName(String commodityName) {
        return commodityRepository.readCommodityByName(commodityName);
    }

    public CommodityDomain updateCommodity(CommodityDomain commodityDomain) {
        commodityRepository.updateCommodity(commodityDomain);
        return commodityDomain;
    }

}
