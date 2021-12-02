package com.infra.fdfs.utils;

import lombok.extern.slf4j.Slf4j;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * FastDFS工具类
 *
 * @author: Fimon
 **/
@Slf4j
public class FastDFSUtils {

  // 初始化客户端
  static {
    try {
      ClassPathResource classPathResource = new ClassPathResource("fdfs_client.conf");

      String tempPath =System.getProperty("java.io.tmpdir") + System.currentTimeMillis() + ".conf";
      File f = new File(tempPath);
      FileCopyUtils.copy(classPathResource.getInputStream(), new FileOutputStream(f));
      ClientGlobal.init(f.getAbsolutePath());
    } catch (Exception e) {
      log.error("init fastdfs fail: {}", e.getMessage());
    }
  }

  /**
  * 删除文件：-1：失败；0：成功
  *
  * @param: [groupName, remoteFileName]
  * @return: int
  * @author: Fimon
  */
  public static int delete(String groupName, String remoteFileName) {
    int result = -1;
    try {
      StorageClient storageClient = getStorageClient();
      result = storageClient.delete_file(groupName, remoteFileName);
    } catch (Exception e) {
      log.error("delete file fail: {}", e.getMessage());
    }
    return result;
  }


  /** 
  * 文件下载
  * 
  * @param: [groupName, remoteFileName] 
  * @return: java.io.InputStream 
  * @author: Fimon
  */
  public static InputStream download(String groupName, String remoteFileName) {
    InputStream inputStream = null;
    try {
      StorageClient storageClient = getStorageClient();
      byte[] fileBytes = storageClient.download_file(groupName, remoteFileName);
      inputStream = new ByteArrayInputStream(fileBytes);
    } catch (Exception e) {
      log.error("download file fail: {}", e.getMessage());
    }
    return inputStream;
  }


  /**
  * 文件上传
  *
  * @param: [file]
  * @return: java.lang.String[]
  * @author: Fimon
  */
  public static String[] upload(MultipartFile file) {
    String fileName = file.getOriginalFilename();
    log.info("upload file: {}", fileName);

    String[] results = null;
    try {
      StorageClient storageClient = getStorageClient();
      results = storageClient.upload_file(file.getBytes(),
        fileName.substring(fileName.lastIndexOf('.') + 1), null);
    } catch (Exception e) {
      log.error("upload file fail: {}", e.getMessage());
    }

    return results;
  }

  /** 
  * 获取url前缀
  * 
  * @param: [] 
  * @return: java.lang.String 
  * @author: Fimon
  */
  public static String getTrackerUrl() {
    String url = null;
    try {
      TrackerClient trackerClient = new TrackerClient();
      TrackerServer trackerServer = trackerClient.getConnection();
      StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
      url = "http://" + storageServer.getInetSocketAddress().getHostString() +
        ":8888/";
    } catch (Exception e) {
      log.error("get tracker url fail: {}", e.getMessage());
    }
    return url;
  }

  /**
  * 生成storage客户端
  *
  * @param: []
  * @return: org.csource.fastdfs.StorageClient
  * @author: Fimon
  */
  private static StorageClient getStorageClient() throws IOException {
    TrackerServer trackerServer = getTrackerServer();
    StorageClient storageClient = new StorageClient(trackerServer, null);
    return storageClient;
  }

  /**
  * 生成tracker服务器
  *
  * @param: []
  * @return: org.csource.fastdfs.TrackerServer
  * @author: Fimon
  */
  private static TrackerServer getTrackerServer() throws IOException {
    TrackerClient trackerClient = new TrackerClient();
    TrackerServer trackerServer = trackerClient.getConnection();
    return trackerServer;
  }
}
