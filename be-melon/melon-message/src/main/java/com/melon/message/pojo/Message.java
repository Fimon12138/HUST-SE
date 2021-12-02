package com.melon.message.pojo;

import com.melon.common.pojo.message.TransferMessage;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Message extends TransferMessage {

    private Timestamp generateTimestamp;

    private Timestamp handleTimestamp;

    private Integer status;

    private Timestamp updateTimestamp;

}
