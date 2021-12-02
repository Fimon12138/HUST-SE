package com.melon.user.controller.inner;

import com.melon.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
// 远程服务接口
@RequestMapping("/feign")
public class QueryUserDetailsController {

    @Autowired
    private UserService userService;

    @GetMapping("/details/query/{userId}")
    public String queryUserDetails(@PathVariable("userId") String userId) {
        log.info("query details of user {}", userId);
        return userService.queryUserDetails(userId);
    }

    @PostMapping("/details/add/batch")
    public String getUserDetailsInBatch(
            @RequestParam("params") String params,
            @RequestParam("fromKey") String fromKey,
            @RequestParam("toKey") String toKey
    ) {
        log.info("add user details in batch for param {} from field {} to field {}",
                params, fromKey, toKey);
        return userService.addUserDetailsInBatch(params, fromKey, toKey);
    }

    @PostMapping("/details/add/one")
    public String getUserDetailsOne(
            @RequestParam("param") String param,
            @RequestParam("fromKey") String fromKey,
            @RequestParam("toKey") String toKey
    ) {
        log.info("add user details for {} from field {} to field {}", param, fromKey, toKey);
        return userService.addUserDetailsOne(param, fromKey, toKey);
    }

}
