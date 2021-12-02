package com.melon.message.mapper;

import com.melon.common.pojo.FetchParams;
import com.melon.message.pojo.Message;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface QueryMessageMapper {

    @Select("select now()")
    Timestamp getCurrentTimestamp();

    @Select("select * " +
            "from t_message " +
            "where to_user_id=#{userId} and status=2 " +
            "limit 100")
    Message[] fetchMessageUnread(String userId);

    @Select("select * " +
            "from t_message " +
            "where to_user_id=#{userId} and " +
            "unix_timestamp(handle_timestamp)<=unix_timestamp(#{timestamp}) and " +
            "status<=2 " +
            "limit #{pageSize} offset #{offset}")
    Message[] fetchMessageAll(FetchParams fetchParams);

    @Select("select to_user_id " +
            "from t_message " +
            "where id=#{messageId}")
    String getToUserIdByMessageId(String messageId);

    @Select("select count(*) " +
            "from t_message " +
            "where id=#{messageId}")
    Integer getMessageNum(String messageId);

}
