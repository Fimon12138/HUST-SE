package com.melon.activity.service;

import com.melon.common.pojo.FetchParams;
import com.melon.common.pojo.Response;

public interface FetchService {

    public Response fetchActivities(FetchParams fetchParams);

    // 查询单个活动
    public Response getActivityDetails(String userId, String activityId);


}
