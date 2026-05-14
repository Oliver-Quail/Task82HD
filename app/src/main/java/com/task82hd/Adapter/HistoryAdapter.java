package com.task82hd.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.task82hd.Database.Entity.Chat;
import com.task82hd.R;
import com.task82hd.Database.Entity.Message;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    Context context;
    ArrayList<Chat> chats;

    public HistoryAdapter(Context context, ArrayList<Chat> chats) {
        this.context = context;
        this.chats = chats;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_history, parent, false);
        return new HistoryAdapter.HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryHolder holder, int position) {
        holder.nameText.setText(chats.get(position).getName());
        holder.chatId = chats.get(position).chatId;


    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class HistoryHolder extends RecyclerView.ViewHolder {

        TextView nameText;
        Button viewButton;
        int chatId;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.name_text);
            viewButton = itemView.findViewById(R.id.view_button);

            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("chatId", chatId);
                    NavController navController = Navigation.findNavController(itemView);
                    navController.navigate(R.id.view_history);
                }
            });
        }


    }
}