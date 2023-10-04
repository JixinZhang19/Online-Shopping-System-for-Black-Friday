package com.skillup.api.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommodityOutDto {
    String commodityId;

    String commodityName;

    String description;

    Integer price;

    String imageUrl;
}
