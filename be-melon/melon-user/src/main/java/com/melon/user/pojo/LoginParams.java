package com.melon.user.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Component
@ApiModel(value = "用户登录参数")
@Data
public class LoginParams {

    @NotBlank(message = "用户名不能为空")
    @Length(max=100, message="用户名最长100个字符")
    private String username;

    @NotBlank(message = "用户密码不能为空")
    private String password;

}
