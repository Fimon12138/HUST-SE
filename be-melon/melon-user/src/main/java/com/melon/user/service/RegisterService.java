package com.melon.user.service;

import com.melon.common.pojo.Response;
import com.melon.user.pojo.LoginParams;

public interface RegisterService {

    public Response isUsernameRegistered(String username);

    public Response register(LoginParams loginParams);


}
