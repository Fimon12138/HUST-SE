package com.melon.activity.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.melon.activity.mapper.RegisterMapper;
import com.melon.activity.mapper.RegisterRouterMapper;
import com.melon.activity.pojo.params.RegisterParams;
import com.melon.activity.pojo.register.HandleStep;
import com.melon.activity.pojo.register.RegisterStatus;
import com.melon.activity.utils.MessageProducer;
import com.melon.common.pojo.Response;
import com.melon.activity.service.RegisterService;
import com.melon.common.pojo.message.MessageType;
import com.melon.common.tools.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author: Fimon
 **/
@Service
@Slf4j
public class RegisterServiceImpl implements RegisterService {

  @Autowired
  private RegisterMapper registerMapper;

  @Autowired
  private RegisterRouterMapper registerRouterMapper;

  @Autowired
  private IdGenerator idGenerator;

  @Autowired
  private MessageProducer messageProducer;

  // 判断当前用户是否已注册过该活动
  private String getRegisterId(String userId, String activityId) {
    String res = registerMapper.getRegisterId(userId, activityId);
    return res;
  }

  @Override
  @Transactional
  public Response initRegister(String userId, RegisterParams params) {
    if (getRegisterId(userId, params.getActivityId()) != null) {
      log.error("user {} fails to register to activity {}: already registered",
              userId, params.getActivityId());
      return Response.error("报名失败：不可重复报名");
    }
    String id = idGenerator.nextId();
    registerMapper.initRegister(id, userId, params.getActivityId());

    HandleStep handleStep = new HandleStep(idGenerator.nextId(),
            id, true, params.getMessage(), RegisterStatus.INIT.statusValue, null);
    registerRouterMapper.addOneStep(handleStep);

    // 通知活动所有者
    messageProducer.sendRegisterMessage(MessageType.INITREGISTER.val, userId, params.getMessage(),
            params.getActivityId(), id);

    log.info("user {} register to activity {} successfully", userId, params.getActivityId());
    return Response.success("报名成功");
  }

  @Override
  @Transactional
  public Response cancelRegister(String userId, RegisterParams params) {
    String id = getRegisterId(userId, params.getActivityId());
    if (id != null) {
      // 有进行中的报名
      registerMapper.cancelRegister(id, RegisterStatus.CANCELBYREGISTER.statusValue);
      HandleStep handleStep = new HandleStep(idGenerator.nextId(),
              id, true, params.getMessage(), RegisterStatus.CANCELBYREGISTER.statusValue, null);
      registerRouterMapper.addOneStep(handleStep);

      // 通知活动所有者
      messageProducer.sendRegisterMessage(MessageType.CANCELREGISTER.val, userId, params.getMessage(),
              params.getActivityId(), id);

      log.info("user {} cancels register {} successfully", userId, id);
      return Response.success("取消报名成功");
    }
    log.error("user {} cancels register to activity {} failed: haven't registered",
            userId, params.getActivityId());
    return Response.error("未报名此活动");
  }

  public Integer getRegisterStatus(String userId, String activityId) {
    Integer status = registerMapper.getRegisterStatus(userId, activityId);
    return status;
  }


}
