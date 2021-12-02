package com.melon.user.service;

import com.melon.common.pojo.Response;
import com.melon.user.pojo.LoginParams;

public interface LoginService {

    public Response login(LoginParams loginParams);

}
