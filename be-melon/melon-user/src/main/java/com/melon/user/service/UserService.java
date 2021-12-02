package com.melon.user.service;

import com.alibaba.fastjson.JSONArray;
import com.melon.common.pojo.Response;

public interface UserService {

    public Response getUserDetails(String userId);

    public String queryUserDetails(String userId);

    public String addUserDetailsInBatch(String params, String fromKey, String toKey);

    public String addUserDetailsOne(String param, String fromKey, String toKey);

}
