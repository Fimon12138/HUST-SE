package com.melon.user.pojo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ApiModel(value = "注册时所需的参数")
public class RegisterParams {

    private String id;

    private String username;

    private String password;

}
