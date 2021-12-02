package com.melon.common.pojo.message;

public enum MessageType {

    INITREGISTER(0),
    PENDINGREGISTER(1),
    APPROVEREGISTER(2),
    REJECTREGISTER(3),
    CANCELREGISTER(4),
    CANCELREGISTERBYSPONSOR(5),
    USERMESSAGE(6);

    public final int val;

    private MessageType(int val) {
        this.val = val;
    }

}
