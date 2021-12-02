package com.melon.message.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("melon-user")
public interface UserFeignService {

    @GetMapping("/feign/details/query/{userId}")
    public String queryUserDetails(@PathVariable("userId") String userId);

}
