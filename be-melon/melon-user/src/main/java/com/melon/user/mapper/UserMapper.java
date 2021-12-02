package com.melon.user.mapper;

import com.melon.common.pojo.UserDetails;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    @Select("select *, ST_AsText(location_point) as location_point " +
            "from t_user " +
            "where id=#{userId}")
    @Results({
            @Result(property = "location.id", column = "location_id"),
            @Result(property = "location.point", column = "location_point"),
            @Result(property = "location.desc", column = "location_desc"),
            @Result(property = "location.precision", column = "location_precision"),
    })
    UserDetails loadUserByUserId(String userId);

}
