package com.melon.activity.pojo.activity;

public enum ActivityStatus {

    INIT(0),
    // 活动时间已过
    CLOSED(1);

    public final int statusValue;

    private ActivityStatus(int statusValue) {
        this.statusValue = statusValue;
    }
}
