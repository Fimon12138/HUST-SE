package com.melon.message.service;

import com.melon.common.pojo.Response;
import com.melon.message.pojo.SendMessageParams;

public interface HandleMessageService {

    public Response sendMessage(String userId, SendMessageParams params);

    public Response readMessage(String userId, String messageId);

    public boolean withdrawMessageFeign(String messageId);

    public Response withdrawMessage(String userId, String messageId);

    public Response deleteMessage(String userId, String messageId);

}
