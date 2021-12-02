package com.melon.activity.pojo.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SwitchActivityStatusParams {

    @NotBlank(message = "activityId不能为空")
    private String activityId;

    @NotNull(message = "status不能为空")
    private Integer status;

}
