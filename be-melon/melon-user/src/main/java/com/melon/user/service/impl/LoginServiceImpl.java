package com.melon.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.melon.auth.JwtUtils;
import com.melon.common.pojo.Response;
import com.melon.user.mapper.LoginMapper;
import com.melon.user.pojo.LoginParams;
import com.melon.common.pojo.UserDetails;
import com.melon.user.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    private String tokenHeader = "Authorization";

    @Override
    public Response login(LoginParams loginParams) {
        UserDetails userDetails = loginMapper.loadUserByUsername(loginParams.getUsername());

        if (userDetails == null ||
                !passwordEncoder.matches(loginParams.getPassword(), userDetails.getPassword())) {
            return Response.error("用户名或密码错误");
        }
        if (!userDetails.getEnabled()) {
            return Response.error("该账户已被禁用");
        }
        // 登录成功
        // 密码保护
        userDetails.setPassword(null);
        // 将用户信息放入 Redis
        try {
            String s = JSON.toJSONString(userDetails);
            stringRedisTemplate.opsForValue().set(userDetails.getId(), s);
        } catch (Exception e) {
            log.error("redis error: try to put user details to redis fail");
        }

        // 生成 token
        String token = jwtUtils.generateToken(userDetails.getUsername(), userDetails.getId());

        // 返回 token
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        result.put("tokenHeader", tokenHeader);
        return Response.success("登录成功", result);
    }

}
