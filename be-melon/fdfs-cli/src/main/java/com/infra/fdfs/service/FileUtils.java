package com.infra.fdfs.service;

import com.infra.fdfs.mapper.FileHandlerMapper;
import com.infra.fdfs.utils.FastDFSUtils;
import com.infra.fdfs.utils.MD5Utils;
import com.melon.common.tools.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传工具类
 * 封装 fastdfs 和 md5 实现去重
 *
 * @author: Fimon
 **/
@Slf4j
@Service
public class FileUtils {

  @Autowired
  private FileHandlerMapper fileHandlerMapper;

  @Autowired
  private IdGenerator idGenerator;

  /** 
  * 上传单张图片，返沪图片url
  * 
  * @param: [file] 
  * @return: java.lang.String 
  * @author: Fimon
  */
  @Transactional
  public String fileUpload(MultipartFile file) {
    if (file == null) {
      return null;
    }

    // 生成文件的 md5
    String md5 = MD5Utils.getFileMD5Password(file);

    if (md5 != null) {
      String fileUrl = fileHandlerMapper.getFileUrlByMD5(md5);
      if (fileUrl != null) {
        // 图片已存在，直接返回url
        return fileUrl;
      } else {
        // 图片不存在，上传图片
        String[] filePath = FastDFSUtils.upload(file);
        if (filePath != null) {
          String fileId = idGenerator.nextId();
          fileUrl = FastDFSUtils.getTrackerUrl() + filePath[0] + "/" + filePath[1];
          fileHandlerMapper.insertFileInfo(fileId, md5, fileUrl);
          log.info("upload file {} successfully", fileUrl);
          return fileUrl;
        }
      }
    }

    log.error("file upload fail");
    return null;
  }


  /**
  * 上传多张图片
  *
  * @param: [fileList]
  * @return: java.lang.String[]
  * @author: Fimon
  */
  public String filesUpload(MultipartFile[] fileList) {

    String[] fileUrls = new String[fileList.length];
    for (int i=0; i<fileList.length; i++) {
      String url = fileUpload(fileList[i]);
      fileUrls[i] = url;
    }

    return String.join(",", fileUrls);
  }


  /**
  * 根据图片 id 获取 url
  *
  * @param: [fileId]
  * @return: java.lang.String
  * @author: Fimon
  */
  public String getFileUrlById(String fileId) {
    String fileUrl = fileHandlerMapper.getFileUrlById(fileId);
    return fileUrl;
  }
}
