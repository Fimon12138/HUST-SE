package com.melon.common.pojo.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferMessage {

    private String id;

    private Integer type;

    private String fromUserId;

    private String toUserId;

    private String hint;

    private String message;

    private String reference;

}
