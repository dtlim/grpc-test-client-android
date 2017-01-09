package com.voyagerinnovation.grpcclient.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.voyagerinnovation.grpcclient.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dale on 1/9/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;
    private List<ChatMessage> messageList = new ArrayList<>();

    public ChatAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        holder.textViewName.setText(message.getName() + ":");
        holder.textViewMessage.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void setMessageList(List<ChatMessage> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    public void addMessage(ChatMessage message) {
        messageList.add(message);
        notifyItemChanged(messageList.size());
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewMessage;

        ChatViewHolder(View view) {
            super(view);
            textViewName = (TextView) view.findViewById(R.id.textview_name);
            textViewMessage = (TextView) view.findViewById(R.id.textview_message);
        }
    }
}
