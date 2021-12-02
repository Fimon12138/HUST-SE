package com.melon.activity.pojo.register;

public enum RegisterStatus {

    INIT(0),
    PENDING(1),
    APPROVE(2),
    REJECT(3),
    CANCELBYREGISTER(4),
    CANCELBYSPONSOR(5);

    public final int statusValue;

    private RegisterStatus(int value) {
        this.statusValue = value;
    }

}
