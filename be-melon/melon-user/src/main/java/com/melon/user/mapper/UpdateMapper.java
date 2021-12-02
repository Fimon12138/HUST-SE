package com.melon.user.mapper;

import com.melon.user.pojo.UpdateParams;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateMapper {

    @Update("update t_user " +
            "set avatar_url=#{avatarUrl} " +
            "where id=#{userId}")
    void updateAvatar(String userId, String avatarUrl);


    @Update({"<script>",
            "update t_user ",
            " <set>",
            "   <if test='u.email != null'>email=#{u.email}, </if>",
            "   <if test='u.school != null'>school=#{u.school}, </if>",
            "   <if test='u.age != null'>age=#{u.age}, </if>",
            "   <if test='u.gender != null'>gender=#{u.gender}, </if>" +
            "   <if test='u.location and u.location.id != null'>location_id=#{u.location.id}, </if>",
            "   <if test='u.location and u.location.point != null'>location_point=ST_PointFromText(#{u.location.point}), </if>",
            "   <if test='u.location and u.location.desc != null'>location_desc=#{u.location.desc}, </if>",
            "   <if test='u.location and u.location.precision!= null'>location_precision=#{u.location.precision} </if>",
            " </set>",
            "where id=#{id}",
            "</script>"
    })
    void updateUserDetails(String id, @Param("u") UpdateParams updateParams);


//    "   <if test='user.lastLocation != null and user.lastLocation.point != null'>location_point=ST_GeomFromText(#{user.lastLocation.point}), </if>",
//    "   <if test='user.lastLocation != null and user.lastLocation.desc != null'>location_desc=#{user.lastLocation.desc}, </if>",
//    "   <if test='user.lastLocation != null and user.lastLocation.precision != null'>location_precision=#{user.lastLocation.precision} </if>",
}
