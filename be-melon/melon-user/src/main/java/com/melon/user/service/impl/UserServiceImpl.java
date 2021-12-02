package com.melon.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.melon.common.pojo.Response;
import com.melon.common.pojo.UserDetails;
import com.melon.user.mapper.UserMapper;
import com.melon.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserMapper userMapper;


    // 公用方法，获取用户信息
    private UserDetails getUserDetailsCommon(String userId) {
        if (userId == null) {
            return null;
        }

        UserDetails userDetails = null;
        // 先从 redis 中查
        String str = stringRedisTemplate.opsForValue().get(userId);
        if (!StringUtils.isEmpty(str)) {
            userDetails = JSON.parseObject(str, new TypeReference<UserDetails>(){});
        } else {
            // 从 db 中查
            userDetails = userMapper.loadUserByUserId(userId);
            if (userDetails != null) {
                userDetails.setPassword(null);
            }
            // 放入缓存
            stringRedisTemplate.opsForValue().set(userId, JSON.toJSONString(userDetails));
        }
        return userDetails;
    }

    @Override
    public Response getUserDetails(String userId) {
        UserDetails userDetails = getUserDetailsCommon(userId);
        return userDetails != null ? Response.success("查询成功", userDetails) :
                Response.error("查询失败");
    }

    @Override
    public String queryUserDetails(String userId) {
        UserDetails userDetails = getUserDetailsCommon(userId);
        return JSON.toJSONString(userDetails);
    }

    @Override
    public String addUserDetailsInBatch(String params, String fromKey, String toKey) {
        JSONArray array = JSONArray.parseArray(params);
        for (int i=0; i<array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String userId = obj.getString(fromKey);
            UserDetails userDetails = getUserDetailsCommon(userId);
            obj.put(toKey, userDetails);
            obj.remove(fromKey);
        }
        return array.toJSONString();
    }

    @Override
    public String addUserDetailsOne(String param, String fromKey, String toKey) {
        JSONObject obj = JSONObject.parseObject(param);
        String userId = obj.getString(fromKey);
        UserDetails userDetails = getUserDetailsCommon(userId);
        obj.put(toKey, userDetails);
        obj.remove(fromKey);
        return obj.toJSONString();
    }
}
