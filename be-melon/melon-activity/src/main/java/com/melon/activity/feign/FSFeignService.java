package com.melon.activity.feign;

import com.melon.common.pojo.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 远程调用文件存储服务
 *
 * @author: Fimon
 **/
@FeignClient("fdfs-cli")
public interface FSFeignService {

  @PostMapping(value = "/upload/multi", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  Response uploadFiles(
    @RequestPart("files") MultipartFile[] files
  );

}
