package com.melon.activity.service;

import com.melon.activity.pojo.activity.ActivityEdit;
import com.melon.activity.pojo.activity.ActivityUpload;
import com.melon.activity.pojo.params.SwitchActivityStatusParams;
import com.melon.common.pojo.Response;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: Fimon
 **/
public interface ActivityService {

  public Response uploadActivity(String userId, ActivityUpload activityUpload, MultipartFile[] files);

  public Response editActivity(String userId, ActivityEdit activityEdit, MultipartFile[] files);

  public Response delActivity(String userId, String activityId);

  // 发布者修改活动状态
  public Response switchActivityStatus(String userId, SwitchActivityStatusParams params);

}
