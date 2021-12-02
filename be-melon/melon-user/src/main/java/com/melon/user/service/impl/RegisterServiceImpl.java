package com.melon.user.service.impl;

import com.melon.common.pojo.Response;
import com.melon.common.tools.IdGenerator;
import com.melon.user.mapper.RegisterMapper;
import com.melon.user.pojo.LoginParams;
import com.melon.user.pojo.RegisterParams;
import com.melon.user.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private RegisterMapper registerMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public Response isUsernameRegistered(String username) {
        String userId = registerMapper.findUserByUsername(username);
        return Response.success("查询成功", userId == null);
    }

    @Override
    // 两次查表
    @Transactional
    public Response register(LoginParams loginParams) {
        // 判断是否可注册
        String userId = registerMapper.findUserByUsername(loginParams.getUsername());
        if (userId == null) {
            // 生成userId
            String newId = idGenerator.nextId();

            // 密码加密
            String encodedPassword = new BCryptPasswordEncoder().encode(loginParams.getPassword());

            registerMapper.register(new RegisterParams(newId, loginParams.getUsername(), encodedPassword));

            return Response.success("注册成功");
        }

        return Response.error("注册失败：该用户名已被占用");
    }
}
