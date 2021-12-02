package com.melon.activity.service.impl;

import com.alibaba.fastjson.JSON;
import com.melon.activity.mapper.FetchMapper;
import com.melon.activity.mapper.HandleRegisterMapper;
import com.melon.activity.pojo.activity.ActivityResp;
import com.melon.activity.pojo.register.RegisterResp;
import com.melon.activity.service.FeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FeignServiceImpl implements FeignService {

    @Autowired
    private FetchMapper fetchMapper;

    @Autowired
    private HandleRegisterMapper handleRegisterMapper;

    @Override
    public String queryActivityDetails(String activityId) {
        ActivityResp activityResp = fetchMapper.getActivityDetails(activityId);
        log.info("query details of activity {} successfully", activityId);
        return JSON.toJSONString(activityResp);
    }

    @Override
    public String queryRegisterDetails(String registerId) {
        RegisterResp registerResp = handleRegisterMapper.getRegisterDetails(registerId);
        log.info("query details of register successfully", registerId);
        return JSON.toJSONString(registerResp);
    }
}
