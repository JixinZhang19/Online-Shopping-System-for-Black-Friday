package com.skillup.api;

import com.skillup.api.dto.in.CommodityInDto;
import com.skillup.api.dto.out.CommodityOutDto;
import com.skillup.api.util.SkillUpCommon;
import com.skillup.api.util.SkillUpResponse;
import com.skillup.domain.commodity.CommodityDomain;
import com.skillup.domain.commodity.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/commodity")
public class CommodityController {

    @Autowired
    CommodityService commodityService;

    @PostMapping
    public ResponseEntity<SkillUpResponse> createCommodity(@RequestBody CommodityInDto commodityInDto) {
        CommodityDomain commodityDomain = commodityService.createCommodity(toDomain(commodityInDto));
        return ResponseEntity
                .status(SkillUpCommon.SUCCESS)
                .body(SkillUpResponse.builder().result(toOutDto(commodityDomain)).msg(null).build());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<SkillUpResponse> readCommodityById(@PathVariable("id") String commodityId) {
        CommodityDomain commodityDomain = commodityService.readCommodityById(commodityId);
        if (Objects.isNull(commodityDomain)) {
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(null);
        }
        return ResponseEntity
                .status(SkillUpCommon.SUCCESS)
                .body(SkillUpResponse.builder().result(toOutDto(commodityDomain)).msg(null).build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SkillUpResponse> readCommodityByName(@PathVariable("name") String commodityName) {
        CommodityDomain commodityDomain = commodityService.readCommodityByName(commodityName);
        if (Objects.isNull(commodityDomain)) {
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(null);
        }
        return ResponseEntity
                .status(SkillUpCommon.SUCCESS)
                .body(SkillUpResponse.builder().result(toOutDto(commodityDomain)).msg(null).build());
    }

    @PutMapping("/update")
    public ResponseEntity<SkillUpResponse> updateCommodity(@RequestBody CommodityInDto commodityInDto) {
        CommodityDomain commodityDomain = commodityService.readCommodityByName(commodityInDto.getCommodityName());

        commodityDomain.setDescription(commodityInDto.getDescription());
        commodityDomain.setPrice(commodityInDto.getPrice());
        commodityDomain.setImageUrl(commodityInDto.getImageUrl());

        commodityService.updateCommodity(commodityDomain);

        return ResponseEntity
                .status(SkillUpCommon.SUCCESS)
                .body(SkillUpResponse.builder().result(toOutDto(commodityDomain)).msg(null).build());
    }


    private CommodityDomain toDomain(CommodityInDto commodityInDto) {
        return CommodityDomain.builder()
                .commodityId(UUID.randomUUID().toString())
                .commodityName(commodityInDto.getCommodityName())
                .description(commodityInDto.getDescription())
                .price(commodityInDto.getPrice())
                .imageUrl(commodityInDto.getImageUrl())
                .build();
    }

    private CommodityOutDto toOutDto(CommodityDomain commodityDomain) {
        return CommodityOutDto.builder()
                .commodityId(commodityDomain.getCommodityId())
                .commodityName(commodityDomain.getCommodityName())
                .description(commodityDomain.getDescription())
                .price(commodityDomain.getPrice())
                .imageUrl(commodityDomain.getImageUrl())
                .build();
    }










}
