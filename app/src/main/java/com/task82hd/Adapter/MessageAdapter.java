package com.task82hd.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.task82hd.R;
import com.task82hd.Database.Entity.Message;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {
    Context context;
    ArrayList<Message> messages;

    public MessageAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_message, parent, false);
        return new MessageAdapter.MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageHolder holder, int position) {

        holder.messageText.setText(messages.get(position).contents);

        if(!messages.get(position).isAi) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.END;

            holder.messageText.setLayoutParams(params);
            holder.timeSent.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeSent;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.message_text);
            timeSent = itemView.findViewById(R.id.time_sent);

        }

        public void setIsAIMessage(boolean isAIMessage) {

            if(!isAIMessage) {

            }
        }
    }
}