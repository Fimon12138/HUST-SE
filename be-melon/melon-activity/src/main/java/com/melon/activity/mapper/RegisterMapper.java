package com.melon.activity.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Fimon
 **/
@Repository
public interface RegisterMapper {

  // 用于判断当前用户是否已注册过该活动，
  // 避免重复注册
  @Select("select id " +
          "from t_register " +
          "where activity_id=#{activityId} and " +
          "user_id=#{userId} and " +
          "is_close=false")
  String getRegisterId(String userId, String activityId);

  @Select("select status " +
          "from t_register " +
          "where activity_id=#{activityId} and " +
                "user_id=#{userId} and " +
                "is_close=false")
  Integer getRegisterStatus(String userId, String activityId);

  // 注册活动
  @Insert("insert into t_register " +
          "(id, activity_id, user_id) " +
          "values " +
          "(#{id}, #{activityId}, #{userId})")
  void initRegister(String id, String userId, String activityId);

  // 更新注册状态
  @Update("update t_register " +
          "set " +
              "status=#{status}, " +
              "is_close=true " +
          "where id=#{id}")
  void cancelRegister(String id, Integer status);


}
