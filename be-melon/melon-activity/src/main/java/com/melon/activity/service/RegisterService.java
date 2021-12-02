package com.melon.activity.service;

import com.melon.activity.pojo.params.RegisterParams;
import com.melon.common.pojo.Response;

/**
 * @author: Fimon
 **/
public interface RegisterService {

  // 发起一次报名活动
  public Response initRegister(String userId, RegisterParams params);

  // 取消报名活动
  public Response cancelRegister(String userId, RegisterParams params);


}
