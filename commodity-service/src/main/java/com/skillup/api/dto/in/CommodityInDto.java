package com.skillup.api.dto.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommodityInDto {
    String commodityName;

    String description;

    Integer price;

    String imageUrl;
}
