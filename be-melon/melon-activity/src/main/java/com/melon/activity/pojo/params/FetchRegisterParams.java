package com.melon.activity.pojo.params;

import com.melon.common.pojo.FetchParams;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FetchRegisterParams extends FetchParams {

    @NotBlank(message = "activityId不能为空")
    private String activityId;

    // 是否查询已关闭的报名请求
    private Boolean all = false;

}
