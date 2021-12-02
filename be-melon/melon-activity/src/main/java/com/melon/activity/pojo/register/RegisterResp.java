package com.melon.activity.pojo.register;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Component
public class RegisterResp {

    private String id;

    private String activityId;

    private String userId;

    private Integer status;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp registerTimestamp;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp lastUpdateTimestamp;

    private Boolean isClose;

}
