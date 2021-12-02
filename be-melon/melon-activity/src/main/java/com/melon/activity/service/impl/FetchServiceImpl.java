package com.melon.activity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.melon.activity.feign.UserFeignService;
import com.melon.activity.mapper.FetchMapper;
import com.melon.activity.mapper.RegisterMapper;
import com.melon.activity.pojo.activity.ActivityResp;
import com.melon.activity.service.FetchService;
import com.melon.common.pojo.FetchParams;
import com.melon.common.pojo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class FetchServiceImpl implements FetchService {

    @Autowired
    private FetchMapper fetchMapper;

    @Autowired
    private RegisterServiceImpl registerService;

    @Autowired
    private UserFeignService userFeignService;

    @Override
    public Response fetchActivities(FetchParams fetchParams) {
        // 首次查询时获取系统时间戳
        if (fetchParams.getTimestamp() == null) {
            fetchParams.setTimestamp(fetchMapper.getCurrentTimestamp());
        }
        // 返回结果
        Map<String, Object> resp = new HashMap<>();
        resp.put("timestamp", fetchParams.getTimestamp());

        ActivityResp[] activityResps = null;
        if (fetchParams.getLocationPoint() == null) {
            // 未发送位置信息，按时间查
            log.info("user {} tries to fetch activities by time, params: {}",
                    fetchParams.getUserId(), fetchParams.toString());
            activityResps = fetchActivityByTime(fetchParams);
        }
        log.info("user {} tries to fetch activities auto, params: {}, {}",
                fetchParams.getUserId(), fetchParams.getLocationPoint(), fetchParams.toString());
        activityResps = fetchActivityAuto(fetchParams);

        // 添加发布人信息，远程调用
        if (activityResps == null || activityResps.length == 0) {
            resp.put("activities", activityResps);
            return Response.success("拉取活动成功", resp);
        }
        String activityRespsWithUserDetails = addUserDetails(activityResps);
        if (activityRespsWithUserDetails == null) {
            resp.put("activities", activityResps);
            return Response.success("获取用户信息失败", resp);
        }
        resp.put("activities", JSONArray.parseArray(activityRespsWithUserDetails));
        return Response.success("拉取活动成功", resp);
    }


    public ActivityResp[] fetchActivityByTime(FetchParams fetchParams) {
        ActivityResp[] activityResps = null;
        activityResps = fetchParams.getUserId() == null ?
                fetchMapper.fetchActivityByTimeWithoutUserId(fetchParams) :
                fetchMapper.fetchActivityByTime(fetchParams);
        log.info("fetch activities by time successfully: {}", fetchParams.toString());
        return activityResps;
    }


    public ActivityResp[] fetchActivityAuto(FetchParams fetchParams) {
        ActivityResp[] activityResps = null;
        activityResps = fetchParams.getUserId() == null ?
                fetchMapper.fetchActivityAutoWithoutUserId(fetchParams) :
                fetchMapper.fetchActivityAuto(fetchParams);
        log.info("fetch activities auto successfully: {}", fetchParams.toString());
        return activityResps;
    }

    // 远程调用 user 服务
    // 注入 userDetails 信息
    private String addUserDetails(ActivityResp[] activityResps) {
        log.info("try to add uploader details to activities from melon-user");

        String activitiesWithUserDetails = null;
        try {
            activitiesWithUserDetails = userFeignService.addUserDetailsInBatch(
                    JSON.toJSONString(activityResps), "userId", "fromUser");
        } catch (Exception e) {
            log.error("openfeign fail: activities -> /user/details/activities");
        }

        if (activitiesWithUserDetails != null) {
            log.info("get uploader details successfully");
            return activitiesWithUserDetails;
        }
        log.error("get uploader details error");
        return null;
    }

    public Object getActivityDetailsCommon(String userId, String activityId) {
        ActivityResp activityResp = fetchMapper.getActivityDetails(activityId);
        log.info("get activity {} details successfully", activityId);

        if (activityResp == null) {
            // 不存在
            log.error("activity {} doesn't exist", activityId);
            return null;
        }

        // 补充注册信息
        if (userId != null) {
            Integer status = registerService.getRegisterStatus(userId, activityId);
            log.info("get user register status successfully: {}", status);
            activityResp.setRegisterStatus(status);
        }

        // 补充 userDetails
        log.info("try to add uploader details to activities from melon-user");
        String activityRespWithUserDetails = null;
        try {
            activityRespWithUserDetails = userFeignService.addUserDetailsOne(
                    JSON.toJSONString(activityResp), "userId", "fromUser");
        } catch (Exception e) {
            log.error("openfeign fail: activities -> /user/details/activities");
        }
        if (activityRespWithUserDetails == null) {
            log.error("get uploader details from melon-user fail");
            return activityResp;
        } else {
            log.info("get uploader details from melon-user successfully");
            return JSONObject.parseObject(activityRespWithUserDetails);
        }
    }

    @Override
    public Response getActivityDetails(String userId, String activityId) {
        Object res = getActivityDetailsCommon(userId, activityId);
        return res == null ? Response.error("查询失败：不存在此活动") : Response.success("查询成功", res);
    }
}
