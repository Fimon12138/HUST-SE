package com.melon.message.pojo;

public enum MessageStatus {

    INIT(0),
    READ(1),
    DELETE(2),
    WITHDRAW(3);

    public final int val;

    private MessageStatus(int val) {
        this.val = val;
    }

}
