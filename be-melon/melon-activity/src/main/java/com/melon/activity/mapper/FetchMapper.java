package com.melon.activity.mapper;

import com.melon.activity.pojo.activity.ActivityResp;
import com.melon.common.pojo.FetchParams;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import com.melon.common.handler.mybatis.FileTypeHandler;

import java.sql.Timestamp;

@Repository
public interface FetchMapper {

    // 获取系统当前时间
    @Select("select now()")
    Timestamp getCurrentTimestamp();

    // 携带用户信息查询，关联 t_register 表的报名情况
    @Select("select a.*, ST_AsText(a.location_point) as location_point, r.status as register_status " +
            "from t_activity a " +
            "left join t_register r on r.activity_id=a.id and r.user_id=#{userId} " +
            "where unix_timestamp(upload_timestamp)<=unix_timestamp(#{timestamp}) " +
            "order by upload_timestamp desc " +
            "limit #{pageSize} offset #{offset}")
    @Results(id = "activityRespMapRule",
        value = {
            @Result(property = "location.id", column = "location_id"),
            @Result(property = "location.point", column = "location_point"),
            @Result(property = "location.desc", column = "location_desc"),
            @Result(property = "location.precision", column = "location_precision"),
            @Result(property = "fileList", column = "file_list", typeHandler = FileTypeHandler.class)
        }
    )
    ActivityResp[] fetchActivityByTime(FetchParams fetchParams);


    @Select("select *, ST_AsText(location_point) as location_point " +
            "from t_activity " +
            "where unix_timestamp(upload_timestamp)<=unix_timestamp(#{timestamp}) " +
            "order by upload_timestamp desc " +
            "limit #{pageSize} offset #{offset}")
    @ResultMap("activityRespMapRule")
    ActivityResp[] fetchActivityByTimeWithoutUserId(FetchParams fetchParams);


    @Select("select a.*, ST_AsText(a.location_point) as location_point, r.status as register_status, " +
            "activity_rank_overall(a.upload_timestamp, a.location_hash, " +
                                  "#{timestamp}, ST_GeoHash(ST_PointFromText(#{locationPoint}), 10)) as rank_score " +
            "from t_activity a " +
            "left join t_register r on r.activity_id=a.id and r.user_id=#{userId} " +
            "where unix_timestamp(upload_timestamp)<=unix_timestamp(#{timestamp}) " +
            "order by rank_score desc " +
            "limit #{pageSize} offset #{offset}")
    @ResultMap("activityRespMapRule")
    ActivityResp[] fetchActivityAuto(FetchParams fetchParams);


    @Select("select *, ST_AsText(location_point) as location_point, " +
            "activity_rank_overall(upload_timestamp, location_hash, " +
                                "#{timestamp}, ST_GeoHash(ST_PointFromText(#{locationPoint}), 10)) as rank_score " +
            "from t_activity " +
            "where unix_timestamp(upload_timestamp)<=unix_timestamp(#{timestamp}) " +
            "order by rank_score desc " +
            "limit #{pageSize} offset #{offset}")
    @ResultMap("activityRespMapRule")
    ActivityResp[] fetchActivityAutoWithoutUserId(FetchParams fetchParams);


    @Select("select *, ST_AsText(location_point) as location_point " +
            "from t_activity " +
            "where id=#{activityId}")
    @ResultMap("activityRespMapRule")
    ActivityResp getActivityDetails(String activityId);
}
