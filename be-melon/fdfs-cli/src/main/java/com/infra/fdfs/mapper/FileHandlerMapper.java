package com.infra.fdfs.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 图片处理
 *
 * @author: Fimon
 **/
@Repository
public interface FileHandlerMapper {

  @Select("select url from t_file where md5=#{md5}")
  String getFileUrlByMD5(String md5);

  @Insert("insert into t_file (id, md5, url) values (#{id}, #{md5}, #{url})")
  void insertFileInfo(String id, String md5, String url);

  @Select("select url from t_file where id=#{id}")
  String getFileUrlById(String id);

}
