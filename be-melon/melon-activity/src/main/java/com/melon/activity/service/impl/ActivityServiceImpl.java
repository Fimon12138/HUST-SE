package com.melon.activity.service.impl;

import com.melon.activity.feign.FSFeignService;
import com.melon.activity.mapper.ActivityMapper;
import com.melon.activity.pojo.activity.ActivityEdit;
import com.melon.activity.pojo.activity.ActivityUpload;
import com.melon.activity.pojo.params.SwitchActivityStatusParams;
import com.melon.common.pojo.Response;
import com.melon.activity.service.ActivityService;
import com.melon.common.tools.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: Fimon
 **/
@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {

  @Autowired
  private IdGenerator idGenerator;

  @Autowired
  private FSFeignService fsFeignService;

  @Autowired
  private ActivityMapper activityMapper;

  @Override
  public Response uploadActivity(String userId, ActivityUpload activityUpload, MultipartFile[] files) {
    // 生成id
    String id = idGenerator.nextId();

    // 上传图片
    String fileUrlList = null;
    if (files != null && files.length > 0) {
      // 调用远程图片服务
      Response response = null;
      try {
        response = fsFeignService.uploadFiles(files);
      } catch (Exception e) {
        log.error("openfeign err: activity upload -> fdfs upload multi");
        return Response.error("发布活动失败：文件服务异常");
      }

      if (response != null && response.getCode() == 200) {
        fileUrlList = (String) response.getResults();
      } else {
        log.error("activity upload fail: upload files fail, response: {}", response.toString());
        return Response.error("发布活动失败：上传图片失败");
      }
    }
    
    activityMapper.uploadActivity(id, userId, activityUpload, fileUrlList);
    
    log.info("user {} uploaded an activity successfully: id: {}", userId, id);
    return Response.success("发布成功");
  }


  @Override
  @Transactional
  public Response editActivity(String userId, ActivityEdit activityEdit, MultipartFile[] files) {
    if (validateAccess(userId, activityEdit.getOriginId())) {
      // 上传图片
      String fileUrlList = null;
      if (files != null && files.length > 0) {
        Response response = null;
        try {
          response = fsFeignService.uploadFiles(files);
        } catch (Exception e) {
          log.error("openfeign err: activity edit -> fdfs upload multi");
          return Response.error("发布活动失败：文件服务异常");
        }

        if (response != null && response.getCode() == 200) {
          fileUrlList = (String) response.getResults();
        } else {
          return Response.error("编辑活动失败：上传图片失败");
        }
      }

      activityMapper.editActivity(activityEdit, fileUrlList);

      log.info("user {} edits activity {} successfully", userId, activityEdit.getOriginId());
      return Response.success("编辑成功");
    }
    log.info("user {} edits activity {} fail: this activity doesn't belong to him",
      userId, activityEdit.getOriginId());
    return Response.error("编辑活动失败：用户无权操作该活动");
  }


  @Override
  @Transactional
  public Response delActivity(String userId, String activityId) {
    if (validateAccess(userId, activityId)) {
      activityMapper.delActivity(activityId);

      log.info("user {} deletes activity {} successfully", userId, activityId);
      return Response.success("删除成功");
    }
    log.info("user {} deletes activity {} fail: this activity doesn't belong to him",
      userId, activityId);
    return Response.error("删除失败：用户无权操作该活动");
  }

  @Override
  @Transactional
  public Response switchActivityStatus(String userId, SwitchActivityStatusParams params) {
    if (validateAccess(userId, params.getActivityId())) {
      activityMapper.switchActivityStatus(params);

      log.info("user {} switches status of activity {} to {} successfully",
        userId, params.getActivityId(), params.getStatus());
      return Response.success("更新活动状态成功");
    }
    log.info("user {} switches status of activity {} fail: this activity doesn't belong to him",
      userId, params.getActivityId());
    return Response.error("更新活动状态失败：用户无权操作该活动");
  }


  // 判断操作的 activity 是否属于当前 user
  public boolean validateAccess(String userId, String activityId) {
    String uid = activityMapper.getUserIdByActivityId(activityId);
    return userId.equals(uid);
  }


}
