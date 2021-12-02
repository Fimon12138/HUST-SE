package com.melon.message.service;

import com.melon.common.pojo.FetchParams;
import com.melon.common.pojo.Response;

public interface FetchMessageService {

    public Response fetchMessageUnread(String userId);

    public Response fetchMessageAll(FetchParams fetchParams);

}
