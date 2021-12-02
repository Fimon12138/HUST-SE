package com.melon.user.pojo;

import com.melon.common.pojo.Location;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UpdateParams {

    private String email;

    private String school;

    private Integer age;

    private Integer gender;

    private Location location;

}
