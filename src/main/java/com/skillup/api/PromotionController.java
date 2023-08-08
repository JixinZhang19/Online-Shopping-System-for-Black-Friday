package com.skillup.api;

import com.skillup.api.dto.in.PromotionInDto;
import com.skillup.api.dto.out.PromotionOutDto;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/promotion")
public class PromotionController {

    @Autowired
    PromotionService promotionService;



    @PostMapping
    public PromotionOutDto createPromotion(@RequestBody PromotionInDto promotionInDto) {
        PromotionDomain promotionDomain = promotionService.createPromotion(toDomain(promotionInDto));
        return toOutDto(promotionDomain);
    }









    // general function
    private PromotionDomain toDomain(PromotionInDto inDto) {
        return PromotionDomain
                .builder()
                .promotionId(UUID.randomUUID().toString())
                .promotionName(inDto.getPromotionName())
                .commodityId(inDto.getCommodityId())
                .startTime(inDto.getStartTime())
                .endTime(inDto.getEndTime())
                .originalPrice(inDto.getOriginalPrice())
                .promotionalPrice(inDto.getPromotionalPrice())
                .totalStock(inDto.getTotalStock())
                .availableStock(inDto.getAvailableStock())
                .lockStock(inDto.getLockStock())
                .imageUrl(inDto.getImageUrl())
                .status(inDto.getStatus())
                .build();
    }

    private PromotionOutDto toOutDto(PromotionDomain domain) {
        return PromotionOutDto
                .builder()
                .promotionId(domain.getPromotionId())
                .promotionName(domain.getPromotionName())
                .commodityId(domain.getCommodityId())
                .startTime(domain.getStartTime())
                .endTime(domain.getEndTime())
                .totalStock(domain.getTotalStock())
                .availableStock(domain.getAvailableStock())
                .lockStock(domain.getLockStock())
                .originalPrice(domain.getOriginalPrice())
                .promotionalPrice(domain.getPromotionalPrice())
                .imageUrl(domain.getImageUrl())
                .status(domain.getStatus())
                .build();
    }

}
