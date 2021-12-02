package com.melon.message.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("melon-activity")
public interface ActivityFeignService {

    @GetMapping("/feign/details/{activityId}")
    public String queryActivityDetails(@PathVariable("activityId") String activityId);

    @GetMapping("/feign/register/details/{registerId}")
    public String queryRegisterDetails(@PathVariable("registerId") String registerId);

}
