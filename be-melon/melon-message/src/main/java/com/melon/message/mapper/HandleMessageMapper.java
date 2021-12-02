package com.melon.message.mapper;

import com.melon.common.pojo.message.TransferMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface HandleMessageMapper {

    @Insert("insert into t_message " +
            "(id, from_user_id, to_user_id, type, hint, message, reference) " +
            "values " +
            "(#{id}, #{fromUserId}, #{toUserId}, #{type}, #{hint}, #{message}, #{reference})")
    void insertMessage(TransferMessage message);

    @Update("update t_message " +
            "set status=#{status} " +
            "where id=#{messageId}")
    void updateMessageStatus(String messageId, Integer status);

}
