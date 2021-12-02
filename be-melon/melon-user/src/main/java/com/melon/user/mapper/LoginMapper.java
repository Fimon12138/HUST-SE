package com.melon.user.mapper;

import com.melon.common.pojo.UserDetails;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginMapper {

    @Select("select * from t_user " +
            "where username=#{username}")
    UserDetails loadUserByUsername(String username);

}
