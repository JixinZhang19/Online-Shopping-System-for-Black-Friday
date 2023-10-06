package com.skillup.application.promotionFeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("promotion-service")
public interface PromotionServiceApi {

    @PostMapping("/promotion/lock/id/{id}/orderId/{orderId}")
    public ResponseEntity<Boolean> lockStock(@PathVariable("id") String id, @PathVariable("orderId") Long orderId);

    @PostMapping("/promotion/revert/id/{id}/orderId/{orderId}")
    public ResponseEntity<Boolean> revertStock(@PathVariable("id") String id, @PathVariable("orderId") Long orderId);

}
