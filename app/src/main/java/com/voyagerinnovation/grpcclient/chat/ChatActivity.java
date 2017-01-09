package com.voyagerinnovation.grpcclient.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.voyagerinnovation.grpcclient.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dale on 1/9/17.
 */

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerViewChat;
    EditText editTextName;
    EditText editTextMessage;
    Button buttonSend;

    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerViewChat = (RecyclerView) findViewById(R.id.recyclerview_chat);
        editTextName = (EditText) findViewById(R.id.edittext_name);
        editTextMessage = (EditText) findViewById(R.id.edittext_message);
        buttonSend = (Button) findViewById(R.id.button_send);



        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        chatAdapter = new ChatAdapter(this);
        recyclerViewChat.setAdapter(chatAdapter);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter.setMessageList(dummyList());
    }

    private List<ChatMessage> dummyList() {
        List<ChatMessage> list = new ArrayList<>();
        String[] names = new String[]{"Dale", "Miguel", "Dale", "Chuck"};
        String[] messages = new String[]{"Hi", "Hello", "Where's Chuck?", "I'm Here!"};
        for (int i = 0; i < names.length; i++) {
            list.add(new ChatMessage(names[i], messages[i]));
        }
        return list;
    }

    public interface Listener {
        void onReceiveChatMessage(ChatMessage message);
    }
}
