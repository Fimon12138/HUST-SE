package com.melon.user.mapper;

import com.melon.user.pojo.RegisterParams;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterMapper {

    @Select("select id from t_user " +
            "where username=#{username}")
    String findUserByUsername(String username);


    @Insert("insert into t_user (id, username, password) " +
            "values (#{id}, #{username}, #{password})")
    void register(RegisterParams registerParams);
}
