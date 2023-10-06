package com.skillup.api;

import com.skillup.api.dto.in.OrderInDto;
import com.skillup.api.dto.in.OrderStatusInDto;
import com.skillup.api.dto.out.OrderOutDto;
import com.skillup.api.util.SkillUpCommon;
import com.skillup.api.util.SnowFlake;
import com.skillup.application.order.OrderApplication;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import com.skillup.domain.order.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    SnowFlake snowFlake;

    @Autowired
    OrderApplication orderApplication;

    @Autowired
    OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderOutDto> createBuyNowOrder(@Valid @RequestBody OrderInDto orderInDto) {
        System.out.println("-------- order service 1.1 createBuyNowOrder --------");
        OrderDomain orderDomain = orderApplication.createBuyNowOrder(toDomain(orderInDto));
        return ResponseEntity
                .status(SkillUpCommon.SUCCESS)
                .body(toOrderOutDto(orderDomain));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<OrderOutDto> getOrderById(@PathVariable("id") Long id) {
        System.out.println("-------- order service 1.1 getOrderById ---------");
        OrderDomain orderDomain = orderService.getOrderById(id);
        if (Objects.isNull(orderDomain)) {
            ResponseEntity
                    .status(SkillUpCommon.BAD_REQUEST)
                    .body(null);
        }
        return ResponseEntity
                .status(SkillUpCommon.SUCCESS)
                .body(toOrderOutDto(orderDomain));
    }

    @PatchMapping("/pay")
    public ResponseEntity<OrderOutDto> payBuyNowOrder(@RequestBody OrderStatusInDto orderStatusInDto) {
        System.out.println("-------- order service 1.1 payBuyNowOrder ---------");
        OrderDomain orderDomain = orderApplication.payBuyNowOrder(orderStatusInDto.getOrderNumber(), orderStatusInDto.getExistStatus(), orderStatusInDto.getExpectStatus());

        if (Objects.isNull(orderDomain)) {
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(toOrderOutDto(null));
        } else if (orderDomain.getOrderStatus().equals(OrderStatus.PAYED)) {
            return ResponseEntity.status(SkillUpCommon.SUCCESS).body(toOrderOutDto(orderDomain));
        } else if (orderDomain.getOrderStatus().equals(OrderStatus.CREATED)) {
            return ResponseEntity.status(SkillUpCommon.INTERNAL_ERROR).body(toOrderOutDto(orderDomain));
        } else if (orderDomain.getOrderStatus().equals(OrderStatus.OVERTIME)) {
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(toOrderOutDto(orderDomain));
        } else {
            return ResponseEntity.status(SkillUpCommon.INTERNAL_ERROR).body(toOrderOutDto(orderDomain));
        }
    }





    // general function
    private OrderDomain toDomain(OrderInDto orderInDto) {
        return OrderDomain
                .builder()
                .orderNumber(snowFlake.nextId())
                .userId(orderInDto.getUserId())
                .promotionId(orderInDto.getPromotionId())
                .promotionName(orderInDto.getPromotionName())
                .orderAmount(orderInDto.getOrderAmount())
                .orderStatus(OrderStatus.READY)
                .build();
    }

    private OrderOutDto toOrderOutDto(OrderDomain orderDomain) {
        return OrderOutDto
                .builder()
                .orderNumber(orderDomain.getOrderNumber().toString())
                .userId(orderDomain.getUserId())
                .promotionId(orderDomain.getPromotionId())
                .promotionName(orderDomain.getPromotionName())
                .orderAmount(orderDomain.getOrderAmount())
                .orderStatus(orderDomain.getOrderStatus().code)
                .createTime(orderDomain.getCreateTime())
                .payTime(orderDomain.getPayTime())
                .build();
    }

}
