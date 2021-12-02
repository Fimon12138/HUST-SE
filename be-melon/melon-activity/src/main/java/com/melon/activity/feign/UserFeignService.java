package com.melon.activity.feign;

import com.alibaba.fastjson.JSONArray;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("melon-user")
public interface UserFeignService {

    @PostMapping("/feign/details/add/batch")
    public String addUserDetailsInBatch(
            @RequestParam("params") String params,
            @RequestParam("fromKey") String fromKey,
            @RequestParam("toKey") String toKey
    );

    @PostMapping("/feign/details/add/one")
    public String addUserDetailsOne(
            @RequestParam("param") String param,
            @RequestParam("fromKey") String fromKey,
            @RequestParam("toKey") String toKey
    );

}
