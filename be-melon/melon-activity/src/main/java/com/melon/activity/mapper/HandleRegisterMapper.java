package com.melon.activity.mapper;

import com.melon.activity.pojo.params.FetchRegisterParams;
import com.melon.activity.pojo.register.RegisterResp;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface HandleRegisterMapper {

    // 获取系统当前时间
    @Select("select now()")
    Timestamp getCurrentTimestamp();

    @Select("<script>" +
                "select * " +
                "from t_register " +
                "where activity_id=#{activityId} and " +
                "unix_timestamp(last_update_timestamp) &lt;= unix_timestamp(#{timestamp}) " +
                "<if test='!all'>and is_close=true </if>" +
                "order by last_update_timestamp desc " +
                "limit #{pageSize} offset #{offset}" +
            "</script>")
    RegisterResp[] fetchRegister(FetchRegisterParams params);


    @Update("update t_register " +
            "set " +
                "status=#{status}, " +
                "is_close=#{isClose} " +
            "where id=#{id}")
    void updateRegisterStatus(String id, Integer status, Boolean isClose);


    @Update("update t_register " +
            "set is_close=true " +
            "where id=#{id}")
    void closeRegister(String id);


    @Select("select is_close " +
            "from t_register " +
            "where id=#{id}")
    Boolean getCloseStatus(String id);


    @Select("select * " +
            "from t_register " +
            "where id=#{id}")
    RegisterResp getRegisterDetails(String id);



}
