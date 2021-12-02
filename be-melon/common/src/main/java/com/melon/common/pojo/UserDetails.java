package com.melon.common.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserDetails {

    private String id;

    private String username;

    private String password;

    private String email;

    private String school;

    private Integer age;

    private Integer gender;

    private String avatarUrl;

    private Location location;

    private Boolean enabled;

}
