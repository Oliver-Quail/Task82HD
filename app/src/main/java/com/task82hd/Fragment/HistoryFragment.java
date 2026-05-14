package com.task82hd.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.task82hd.Adapter.HistoryAdapter;
import com.task82hd.Database.AppDatabase;
import com.task82hd.Database.Entity.Chat;
import com.task82hd.R;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {

    RecyclerView historyRecycler;
    AppDatabase db;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        historyRecycler = view.findViewById(R.id.history_recycler);
        db = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();

        ArrayList<Chat> chats = new ArrayList<>(db.chatDAO().getChats());

        HistoryAdapter historyAdapter = new HistoryAdapter(requireContext(), chats);

        historyRecycler.setAdapter(historyAdapter);


    }
}