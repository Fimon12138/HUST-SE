package com.melon.activity.service;

import com.melon.activity.pojo.params.FetchRegisterParams;
import com.melon.common.pojo.Response;

public interface HandleRegisterService {

    // 拉取报名信息
    public Response fetchRegister(String userId, FetchRegisterParams params);

    // 更新报名状态
    public Response updateRegisterStatus(String registerId, Integer status, String message);

    // 获取某一条报名信息
    public Response getRegisterDetails(String registerId);



}
