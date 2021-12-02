package com.melon.user.service;

import com.melon.common.pojo.Response;
import com.melon.user.pojo.UpdateParams;
import org.springframework.web.multipart.MultipartFile;

public interface UpdateService {

    public Response updateAvatar(String userId, MultipartFile file);

    public Response updateUserDetails(String userId, UpdateParams updateParams);

}
