package com.melon.activity.mapper;

import com.melon.activity.pojo.activity.Activity;
import com.melon.activity.pojo.activity.ActivityEdit;
import com.melon.activity.pojo.params.SwitchActivityStatusParams;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @author: Fimon
 **/
@Repository
public interface ActivityMapper {

  @Insert("insert into t_activity " +
          "(id, user_id, title, content, type, file_list, time_from, " +
              "time_to, participant_desc, participant_num, fare, location_id," +
                  " location_point, location_desc, location_precision) " +
          "values " +
          "(#{id}, #{userId}, #{a.title}, #{a.content}, #{a.type}, #{fileUrlList}, #{a.timeFrom}, " +
              "#{a.timeTo}, #{a.participantDesc}, #{a.participantNum}, #{a.fare}, " +
                  "#{a.locationId}, ST_PointFromText(#{a.locationPoint}), #{a.locationDesc}, #{a.locationPrecision})")
  void uploadActivity(String id, String userId,
                      @Param("a") Activity activity, String fileUrlList);


  @Update("update t_activity " +
          "set " +
              "title=#{a.title}, " +
              "content=#{a.content}, " +
              "type=#{a.type}, " +
              "file_list=#{fileUrlList}, " +
              "time_from=#{a.timeFrom}, " +
              "time_to=#{a.timeTo}, " +
              "participant_desc=#{a.participantDesc}, " +
              "participant_num=#{a.participantNum}, " +
              "fare=#{a.fare}, " +
              "location_id=#{a.locationId}, " +
              "location_point=ST_PointFromText(#{a.locationPoint}), " +
              "location_desc=#{a.locationDesc}, " +
              "location_precision=#{a.locationPrecision} " +
          "where id=#{a.originId}")
  void editActivity(@Param("a") Activity activity, String fileUrlList);


  @Delete("delete from t_activity where id=#{activityId}")
  void delActivity(String activityId);


  @Update("update t_activity " +
          "set status=#{status} " +
          "where id=#{activityId}")
  void switchActivityStatus(SwitchActivityStatusParams params);


  @Select("select user_id " +
          "from t_activity " +
          "where id=#{activityId}")
  String getUserIdByActivityId(String activityId);


}
