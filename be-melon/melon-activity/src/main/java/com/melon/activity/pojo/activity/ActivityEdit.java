package com.melon.activity.pojo.activity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ActivityEdit extends ActivityUpload {

    @NotBlank(message = "originId不能为空")
    private String originId;

}
