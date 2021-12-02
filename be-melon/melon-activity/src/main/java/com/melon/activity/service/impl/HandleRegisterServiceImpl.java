package com.melon.activity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.melon.activity.feign.UserFeignService;
import com.melon.activity.mapper.HandleRegisterMapper;
import com.melon.activity.mapper.RegisterRouterMapper;
import com.melon.activity.pojo.params.FetchRegisterParams;
import com.melon.activity.pojo.register.HandleStep;
import com.melon.activity.pojo.register.RegisterResp;
import com.melon.activity.pojo.register.RegisterStatus;
import com.melon.activity.service.HandleRegisterService;
import com.melon.activity.utils.MessageProducer;
import com.melon.common.pojo.Response;
import com.melon.common.tools.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class HandleRegisterServiceImpl implements HandleRegisterService {

    @Autowired
    private HandleRegisterMapper handleRegisterMapper;

    @Autowired
    private ActivityServiceImpl activityService;

    @Autowired
    private UserFeignService userFeignService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private RegisterRouterMapper registerRouterMapper;

    @Autowired
    private FetchServiceImpl fetchService;

    @Autowired
    private MessageProducer messageProducer;

    @Override
    @Transactional
    public Response fetchRegister(String userId, FetchRegisterParams params) {
        if (activityService.validateAccess(userId, params.getActivityId())) {
            if (params.getTimestamp() == null) {
                params.setTimestamp(handleRegisterMapper.getCurrentTimestamp());
            }
            Map<String, Object> resp = new HashMap<>();
            resp.put("timestamp", params.getTimestamp());

            RegisterResp[] registerResps = handleRegisterMapper.fetchRegister(params);
            log.info("fetch register info of activity {} successfully", params.getActivityId());

            // 添加用户信息
            if (registerResps != null && registerResps.length > 0) {
                JSONArray array = addUserDetails(registerResps);
                if (array != null) {
                    log.info("add user details to registers successfully");
                    resp.put("registers", array);
                    return Response.success("查询成功", resp);
                }
                resp.put("registers", registerResps);
                return Response.success("获取用户详情失败", resp);
            }
            resp.put("registers", registerResps);
            return Response.success("查询成功", resp);
        }
        log.error("user {} fails to fetch register info of activity {}: doesn't belong to it",
                userId, params.getActivityId());
        return Response.error("查询失败：用户无权限操作该活动");
    }


    private JSONArray addUserDetails(RegisterResp[] registerResps) {
        String registerWithUserDetails = null;
        try {
            registerWithUserDetails = userFeignService.addUserDetailsInBatch(
                    JSON.toJSONString(registerResps), "userId", "fromUser");
        } catch (Exception e) {
            log.error("openfeign fail: register -> /user/details/activities");
        }
        return registerWithUserDetails == null ? null : JSONArray.parseArray(registerWithUserDetails);
    }


    @Override
    @Transactional
    public Response updateRegisterStatus(String registerId, Integer status, String message) {
        // 检查是否已关闭
        if (handleRegisterMapper.getCloseStatus(registerId)) {
            log.error("fail to update status of register {}: already closed", registerId);
            return Response.error("更新失败：请求已关闭");
        }

        // 更新状态
        boolean isClose = status == RegisterStatus.REJECT.statusValue ||
                status == RegisterStatus.CANCELBYSPONSOR.statusValue;
        handleRegisterMapper.updateRegisterStatus(registerId, status, isClose);
        log.info("update status {} to register {} successfully, close status: {}",
                status, registerId, isClose);

        // 添加一个新的审批步骤
        HandleStep handleStep = new HandleStep(idGenerator.nextId(), registerId, false, message, status, null);
        registerRouterMapper.addOneStep(handleStep);
        log.info("add one handle step {} to register {} successfully", handleStep.toString(), registerId);

        // 发通知
        messageProducer.replyRegisterMessage(status, message, registerId);

        // 结束
        log.info("all steps of update status of register {} are successfully", registerId);
        return Response.success("更新状态成功");
    }


    @Override
    public Response getRegisterDetails(String registerId) {
        RegisterResp registerResp = handleRegisterMapper.getRegisterDetails(registerId);

        if (registerResp == null) {
            log.error("register {} doesn't exist", registerId);
        }
        log.info("get register {} basic details successfully", registerId);

        JSONObject resp = JSONObject.parseObject(JSON.toJSONString(registerResp));

        // 补充 activity
        Object activity = fetchService.getActivityDetailsCommon(null, registerResp.getActivityId());
        if (activity != null) {
            resp.put("activity", activity);
            resp.remove("activityId");
            log.info("add activity {} info to register {} successfully",
                    registerResp.getActivityId(), registerId);
        } else {
            log.error("add activity {} info to register {} fail",
                    registerResp.getActivityId(),registerId);
        }

        // 补充 userDetails
        String registerWithUserDetails = null;
        try {
            registerWithUserDetails = userFeignService.addUserDetailsOne(
                    JSON.toJSONString(resp), "userId", "fromUser");
            log.info("add user details to register {} successfully", registerId);
        } catch (Exception e) {
            log.error("openfeign fail: register -> /user/details/activities");
        }
        if (registerWithUserDetails != null) {
            resp = JSONObject.parseObject(registerWithUserDetails);
        } else {
            log.error("add user details to register {} fail", registerId);
        }

        // 添加中间交互步骤
        HandleStep[] handleSteps = registerRouterMapper.getHandleSteps(registerId);
        log.info("get handle steps of register {} successfully", registerId);
        resp.put("steps", handleSteps);

        log.info("get all info of register {} successfully", registerId);
        return Response.success("查询成功", resp);
    }

}
