package com.skillup.api.dto.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInDto {
    // private Long orderNumber;

    // 订单状态
    // -2:promotion invalid, -1:库存不足订单, 0:预订单, 1:已创建等待付款, 2:已付款, 3:订单过期或者无效
    // private Integer orderStatus;

    @NotBlank(message = "promotionId can not be null")
    private String promotionId;

    private String promotionName;

    private String userId;

    private Integer orderAmount;

    // private LocalDateTime createTime;

    // private LocalDateTime payTime;
}
