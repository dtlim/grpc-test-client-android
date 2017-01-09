package com.voyagerinnovation.grpcclient.chat;

/**
 * Created by dale on 1/9/17.
 */

public interface ChatDataSource {
    void sendChatMessage(ChatMessage message);
    void onReceiveMessage(ChatMessage message);
}
