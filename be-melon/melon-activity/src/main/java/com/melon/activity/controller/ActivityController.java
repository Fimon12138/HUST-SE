package com.melon.activity.controller;

import com.melon.activity.pojo.activity.ActivityEdit;
import com.melon.activity.pojo.activity.ActivityUpload;
import com.melon.activity.pojo.params.SwitchActivityStatusParams;
import com.melon.common.pojo.Response;
import com.melon.activity.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author: Fimon
 **/
@Slf4j
@Api(tags = "发布活动")
@RestController
public class ActivityController {

  @Autowired
  private ActivityService activityService;


  @ApiOperation(value = "发布活动")
  @PostMapping("/upload")
  public Response postActivity(
    @Valid ActivityUpload activityUpload,
    @RequestParam("files") MultipartFile[] files,
    HttpServletRequest request
    ) {
    String userId = request.getHeader("userId");
    log.info("user {} tries to upload activity {}", userId, activityUpload.toString());
    return activityService.uploadActivity(userId, activityUpload, files);
  }


  @ApiOperation(value = "编辑活动")
  @PostMapping("/edit")
  public Response editActivity(
    @Valid ActivityEdit activityEdit,
    @RequestParam("files") MultipartFile[] files,
    HttpServletRequest request
  ) {
    String userId = request.getHeader("userId");
    log.info("user {} tries to edit activity {}", userId, activityEdit.toString());
    return activityService.editActivity(userId, activityEdit, files);
  }


  @ApiOperation(value = "删除活动")
  @GetMapping("/delete/{activityId}")
  public Response delActivity(
    @PathVariable("activityId") String activityId,
    HttpServletRequest request
  ) {
    String userId = request.getHeader("userId");
    log.info("user {} tries to delete activity {}", userId, activityId);
    return activityService.delActivity(userId, activityId);
  }


  @ApiOperation(value = "修改活动状态")
  @PostMapping("/switchstatus")
  public Response switchActivityStatus(
    @RequestBody @Valid SwitchActivityStatusParams params,
    HttpServletRequest request
  ) {
    String userId = request.getHeader("userId");
    log.info("user {} tries to switch status of activity {} to status {}",
      userId, params.getActivityId(), params.getStatus());
    return activityService.switchActivityStatus(userId, params);
  }

}


//    @RequestParam("title") String title,
//    @RequestParam(value = "content", required = false) String content,
//    @RequestParam("type") Integer type,
//    @RequestParam(value = "files", required = false) MultipartFile[] files,
//    @RequestParam("timeFrom") Timestamp timeFrom,
//    @RequestParam("timeTo") Timestamp timeTo,
//    @RequestParam(value = "participantDesc", required = false) String participantDesc,
//    @RequestParam(value = "participantNum", required = false) Integer participantNum,
//    @RequestParam(value = "fare", required = false) Float fare,
//    @RequestParam(value = "location", required = false) Location location,
