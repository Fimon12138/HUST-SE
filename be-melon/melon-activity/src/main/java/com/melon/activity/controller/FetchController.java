package com.melon.activity.controller;

import com.melon.activity.service.FetchService;
import com.melon.common.pojo.FetchParams;
import com.melon.common.pojo.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@Api(tags = "拉取活动")
public class FetchController {

    @Autowired
    private FetchService fetchService;

    @ApiOperation(value = "拉取活动")
    @PostMapping("/fetch")
    public Response fetchActivity(
        @RequestBody @Valid FetchParams params,
        HttpServletRequest request
    ) {
        // 第一页可以不带 timestamp
        // page > 1 必须要带 timestamp
        if (params.getTimestamp() == null && params.getPage() > 1) {
            return Response.error("参数错误：timestamp不能为空");
        }

        String userId = request.getHeader("userId");

        return fetchService.fetchActivities(params);
    }


    @ApiOperation(value = "查询单个活动")
    @GetMapping("/details/{activityId}")
    public Response getActivityDetails(
        @PathVariable("activityId") String activityId,
        HttpServletRequest request
    ) {
        String userId = request.getHeader("userId");
        log.info("user {} tries to get details of activity {}", userId, activityId);
        return fetchService.getActivityDetails(userId, activityId);
    }

}
