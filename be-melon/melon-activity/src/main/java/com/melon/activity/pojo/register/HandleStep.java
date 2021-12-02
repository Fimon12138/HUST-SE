package com.melon.activity.pojo.register;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HandleStep {

    private String id;

    private String registerId;

    private Boolean direction;

    private String message;

    private Integer status;

    private Timestamp timestamp;

}
