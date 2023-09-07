package com.skillup.application.mapper;

import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotionCache.PromotionCacheDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PromotionMapper {
    PromotionMapper INSTANCE = Mappers.getMapper(PromotionMapper.class);

    PromotionCacheDomain toCacheDomain(PromotionDomain promotionDomain);

    PromotionDomain toDomain(PromotionCacheDomain promotionCacheDomain);
}