package com.voyagerinnovation.grpcclient.chat;

import android.text.TextUtils;

/**
 * Created by dale on 1/9/17.
 */

public class ChatMessage {
    private String name;
    private String message;

    public ChatMessage(String name, String message) {
        if(!TextUtils.isEmpty(name)) {
            this.name = name;
        }
        else {
            this.name = "Anonymous";
        }
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
