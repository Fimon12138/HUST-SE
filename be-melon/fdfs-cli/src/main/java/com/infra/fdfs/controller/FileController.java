package com.infra.fdfs.controller;

import com.infra.fdfs.service.FileUtils;
import com.melon.common.pojo.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: Fimon
 **/
@Slf4j
@Api(tags = "上传文件")
@RestController
public class FileController {

  @Autowired
  private FileUtils fileUtils;


  /**
  * 上传单个文件
  *
  * @param: [file]
  * @return: com.infra.fdfs.pojo.Response
  * @author: Fimon
  */
  @ApiOperation(value = "上传单个文件")
  @PostMapping("/upload/one")
  public Response uploadFile(
    @RequestParam("file") MultipartFile file
  ) {
    if (file == null) {
      return Response.error("上传文件为空");
    }

    String url = fileUtils.fileUpload(file);
    if (url != null) {
      return Response.success("上传成功", url);
    }
    return Response.error("上传失败");
  }



  /**
  * 上传多个文件
  *
  * @param: [files]
  * @return: com.infra.fdfs.pojo.Response
  * @author: Fimon
  */
  @ApiOperation(value = "上传多个文件")
  @PostMapping("/upload/multi")
  public Response uploadFiles(
    @RequestParam("files") MultipartFile[] files
  ) {
    if (files == null) {
      return Response.error("上传文件为空");
    }

    String urlList = fileUtils.filesUpload(files);
    return Response.success("上传成功", urlList);
  }
}
