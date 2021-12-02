package com.melon.message.pojo;

import lombok.Data;

@Data
public class SendMessageParams {

    private String toUserId;

    private String message;

}
