package com.melon.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.melon.common.pojo.Location;
import com.melon.common.pojo.Response;
import com.melon.common.pojo.UserDetails;
import com.melon.user.feign.FSFeignService;
import com.melon.user.mapper.UpdateMapper;
import com.melon.user.pojo.UpdateParams;
import com.melon.user.service.UpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UpdateServiceImpl implements UpdateService {

    @Autowired
    private FSFeignService fsFeignService;

    @Autowired
    private UpdateMapper updateMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Response updateAvatar(String userId, MultipartFile file) {
        // 调用远程服务上传图片
        Response response = null;
        try {
            response = fsFeignService.uploadFile(file);
        } catch (Exception e) {
            return Response.error("更新失败：文件服务异常");
        }

        if (response.getCode() != 200) {
            log.error("(openfeign) upload avatar fail");
            return Response.error("更新失败：文件服务异常");
        }

        String avatarUrl = (String) response.getResults();

        // 更新 db
        updateMapper.updateAvatar(userId, avatarUrl);

        // 更新 redis
        updateAvatarUrlInRedis(userId, avatarUrl);

        Map<String, String> results = new HashMap<>();
        results.put("avatarUrl", avatarUrl);
        return Response.success("更新成功", results);
    }


    // 更新 redis 中的用户头像
    private void updateAvatarUrlInRedis(String userId, String avatarUrl) {
        String str = stringRedisTemplate.opsForValue().get(userId);
        if (!StringUtils.isEmpty(str)) {
            UserDetails userDetails = JSON.parseObject(str, new TypeReference<UserDetails>(){});
            userDetails.setAvatarUrl(avatarUrl);

            String newStr = JSON.toJSONString(userDetails);
            stringRedisTemplate.opsForValue().set(userId, newStr);
        }
    }


    @Override
    public Response updateUserDetails(String userId, UpdateParams updateParams) {
        updateMapper.updateUserDetails(userId, updateParams);
        updateUserDetailsInRedis(userId, updateParams);
        return Response.success("更新成功");
    }


    private void updateUserDetailsInRedis(String userId, UpdateParams updateParams) {
        String str = stringRedisTemplate.opsForValue().get(userId);
        if (!StringUtils.isEmpty(str)) {
            UserDetails userDetails = JSON.parseObject(str, new TypeReference<UserDetails>(){});

            if (updateParams.getAge() != null) {
                userDetails.setAge(updateParams.getAge());
            }
            if (updateParams.getEmail() != null) {
                userDetails.setEmail(updateParams.getEmail());
            }
            if (updateParams.getGender() != null) {
                userDetails.setGender(updateParams.getGender());
            }
            if (updateParams.getSchool() != null) {
                userDetails.setSchool(updateParams.getSchool());
            }
            if (updateParams.getLocation() != null) {
                Location newLocation = updateParams.getLocation();
                Location oldLocation = userDetails.getLocation();
                oldLocation = oldLocation == null ? new Location() : oldLocation;

                if (newLocation.getDesc() != null) {
                    oldLocation.setDesc(newLocation.getDesc());
                }
                if (newLocation.getId() != null) {
                    oldLocation.setId(newLocation.getId());
                }
                if (newLocation.getPoint() != null) {
                    oldLocation.setPoint(newLocation.getPoint());
                }
                if (newLocation.getPrecision() != null) {
                    oldLocation.setPrecision(newLocation.getPrecision());
                }

                if (userDetails.getLocation() == null) {
                    userDetails.setLocation(oldLocation);
                }
            }

            String newStr = JSON.toJSONString(userDetails);
            stringRedisTemplate.opsForValue().set(userId, newStr);
        }
    }


}
