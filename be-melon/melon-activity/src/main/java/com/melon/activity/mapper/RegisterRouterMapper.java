package com.melon.activity.mapper;

import com.melon.activity.pojo.register.HandleStep;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterRouterMapper {

    // 插入一条处理记录
    @Insert("insert into t_register_router " +
            "(id, register_id, direction, message, status) " +
            "values " +
            "(#{id}, #{registerId}, #{direction}, #{message}, #{status})")
    void addOneStep(HandleStep step);


    // 查询一个报名活动的审批路径
    @Select("select * " +
            "from t_register_router " +
            "where register_id=#{registerId} " +
            "order by timestamp")
    HandleStep[] getHandleSteps(String registerId);

}
