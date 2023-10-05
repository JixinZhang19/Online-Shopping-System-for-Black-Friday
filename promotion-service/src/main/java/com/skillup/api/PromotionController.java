package com.skillup.api;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.skillup.api.dto.in.PromotionInDto;
import com.skillup.api.dto.mapper.PromotionMapper;
import com.skillup.api.dto.out.PromotionOutDto;
import com.skillup.api.util.SkillUpCommon;
import com.skillup.application.promotion.PromotionApplication;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionService;
import com.skillup.domain.promotionCache.PromotionCacheDomain;
import com.skillup.domain.promotionCache.PromotionCacheService;
import com.skillup.domain.stockCache.StockCacheDomain;
import com.skillup.domain.stockCache.StockCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/promotion")
public class PromotionController {

    @Autowired
    PromotionService promotionService;

    @Autowired
    PromotionApplication promotionApplication;

    @Autowired
    PromotionCacheService promotionCacheService;

    @Autowired
    StockCacheService stockCacheService;


    @PostMapping
    public PromotionOutDto createPromotion(@RequestBody PromotionInDto promotionInDto) {
        PromotionDomain promotionDomain = PromotionMapper.INSTANCE.toDomain(promotionInDto);
        promotionDomain.setPromotionId(UUID.randomUUID().toString());
        return PromotionMapper.INSTANCE.toOutDto(promotionService.createPromotion(promotionDomain));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PromotionOutDto> getById(@PathVariable("id") String id) {
        // cache aside read strategy
        PromotionDomain promotionDomain = promotionApplication.getById(id);
        if (Objects.isNull(promotionDomain)) {
            return ResponseEntity
                    .status(SkillUpCommon.INTERNAL_ERROR)
                    .body(null);
        }
        return ResponseEntity
                .status(SkillUpCommon.SUCCESS)
                .body(PromotionMapper.INSTANCE.toOutDto(promotionDomain));
    }

    @GetMapping("/status/{status}")
    public List<PromotionOutDto> getByStatus(@PathVariable("status") Integer status) {
        List<PromotionDomain> promotionDomainList = promotionService.getByStatus(status);
        // return promotionDomainList.stream().map(this::toOutDto).collect(Collectors.toList());
        return promotionDomainList.stream().map(PromotionMapper.INSTANCE::toOutDto).collect(Collectors.toList());
    }

    @PostMapping("/lock/id/{id}")
    public ResponseEntity<Boolean> lockStock(@PathVariable("id") String id) {
        // ---------------------- 数据库版本 ----------------------
        // 1. check promotion existing
        // PromotionDomain promotionDomain = promotionService.getById(id);
        // if (Objects.isNull(promotionDomain)) {
        //     return ResponseEntity
        //             .status(SkillUpCommon.BAD_REQUEST)
        //             .body(false);
        // }
        // 2.try to lock stock
        // boolean isLocked = promotionService.lockStock(id);
        // if (isLocked) {
        //     return ResponseEntity
        //             .status(SkillUpCommon.SUCCESS)
        //             .body(true);
        // }
        // return ResponseEntity
        //         .status(SkillUpCommon.SUCCESS)
        //         .body(false);

        // ---------------------- redis版本 ----------------------
        // 1. check promotion existing in cache
        PromotionCacheDomain promotionCacheDomain = promotionCacheService.getPromotionById(id);
        if (Objects.isNull(promotionCacheDomain)) {
            return ResponseEntity
                    .status(SkillUpCommon.BAD_REQUEST)
                    .body(false);
        }
        // 2. try to lock stock in cache
        StockCacheDomain stockCacheDomain = StockCacheDomain.builder().promotionId(promotionCacheDomain.getPromotionId()).build();
        boolean isLocked = stockCacheService.lockStock(stockCacheDomain);
        if (isLocked) {
            return ResponseEntity
                    .status(SkillUpCommon.SUCCESS)
                    .body(true);
        }
        return ResponseEntity
                .status(SkillUpCommon.SUCCESS)
                .body(false);
    }

    @PostMapping("/revert/id/{id}")
    public ResponseEntity<Boolean> revertStock(@PathVariable("id") String id) {
        // ---------------------- 数据库版本 ----------------------
        // 1. check promotion existing
        // PromotionDomain promotionDomain = promotionService.getById(id);
        // if (Objects.isNull(promotionDomain)) {
        //     return ResponseEntity
        //             .status(SkillUpCommon.BAD_REQUEST)
        //             .body(false);
        // }
        // 2. try to revert stock
        // boolean isReverted = promotionService.revertStock(id);
        // if (isReverted) {
        //     return ResponseEntity
        //             .status(SkillUpCommon.SUCCESS)
        //             .body(true);
        // }
        // return ResponseEntity
        //         .status(SkillUpCommon.SUCCESS)
        //         .body(false);

        // ---------------------- redis版本 ----------------------
        // 1. check promotion existing in cache
        PromotionCacheDomain promotionCacheDomain = promotionCacheService.getPromotionById(id);
        if (Objects.isNull(promotionCacheDomain)) {
            return ResponseEntity
                    .status(SkillUpCommon.BAD_REQUEST)
                    .body(false);
        }
        // 2. try to revert stock in cache
        StockCacheDomain stockCacheDomain = StockCacheDomain.builder().promotionId(promotionCacheDomain.getPromotionId()).build();
        boolean isReverted = stockCacheService.revertStock(stockCacheDomain);
        if (isReverted) {
            return ResponseEntity
                    .status(SkillUpCommon.SUCCESS)
                    .body(true);
        }
        return ResponseEntity
                .status(SkillUpCommon.SUCCESS)
                .body(false);
    }

    @PostMapping("/deduct/id/{id}")
    public ResponseEntity<Boolean> deductStock(@PathVariable("id") String id) {
        // 1. check promotion existing
        PromotionDomain promotionDomain = promotionService.getById(id);
        if (Objects.isNull(promotionDomain)) {
            return ResponseEntity
                    .status(SkillUpCommon.BAD_REQUEST)
                    .body(false);
        }
        // 2.try to deduct stock
        boolean isDeducted = promotionService.deductStock(id);
        if (isDeducted) {
            return ResponseEntity
                    .status(SkillUpCommon.SUCCESS)
                    .body(true);
        }
        return ResponseEntity
                .status(SkillUpCommon.SUCCESS)
                .body(false);
    }








    // general function
/*
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
*/

}
