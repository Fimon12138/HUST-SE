package com.melon.activity.controller;

import com.melon.activity.service.FeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/feign")
public class FeignController {

    @Autowired
    private FeignService feignService;

    // 查询活动信息
    @GetMapping("/details/{activityId}")
    public String queryActivityDetails(
            @PathVariable("activityId") String activityId
    ) {
        log.info("feign: query details of activity {}", activityId);
        return feignService.queryActivityDetails(activityId);
    }

    @GetMapping("/register/details/{registerId}")
    public String queryRegisterDetails(@PathVariable("registerId") String registerId) {
        log.info("feign: query details of register {}", registerId);
        return feignService.queryRegisterDetails(registerId);
    }


}
