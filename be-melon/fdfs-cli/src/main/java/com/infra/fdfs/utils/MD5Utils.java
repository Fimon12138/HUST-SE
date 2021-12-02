package com.infra.fdfs.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片加密去重
 *
 * @author: Fimon
 **/
@Slf4j
public class MD5Utils {

  /** 
  * 获取图片md5密码，实现去重
  * 
  * @param: [file] 
  * @return: java.lang.String 
  * @author: Fimon
  */
  public static String getFileMD5Password(MultipartFile file) {
    String md5Code = null;
    try {
      byte[] fileBytes = file.getBytes();
      md5Code = DigestUtils.md5DigestAsHex(fileBytes);
    } catch (Exception e) {
      log.error("get file MD5 code fail: {}", e.getMessage());
    }
    return md5Code;
  }
}
