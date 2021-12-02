package com.melon.activity.pojo.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterParams {

    @NotBlank(message = "activityId不能为空")
    private String activityId;

    private String message;

}
