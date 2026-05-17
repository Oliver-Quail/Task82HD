package com.task82hd.Fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.task82hd.Adapter.MessageAdapter;
import com.task82hd.Database.AppDatabase;
import com.task82hd.Database.Entity.Chat;
import com.task82hd.Database.Entity.Message;
import com.task82hd.R;

import java.net.URI;
import java.util.ArrayList;


public class ViewHistoryFragment extends Fragment {

    AppDatabase db;

    TextView chatName;
    ImageView itemImage;
    RecyclerView chatRecyler;
    Button backButton;

    public ViewHistoryFragment() {
        // Required empty public constructor
    }


    public static ViewHistoryFragment newInstance(String param1, String param2) {
        ViewHistoryFragment fragment = new ViewHistoryFragment();
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
        return inflater.inflate(R.layout.fragment_view_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chatName = view.findViewById(R.id.chat_name);
        itemImage = view.findViewById(R.id.item_image);
        chatRecyler = view.findViewById(R.id.chat_recycler);
        backButton = view.findViewById(R.id.back_button);


        int chatId = getArguments().getInt("chatId");

        db = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();

        Chat chat = db.chatDAO().getChat(chatId);

        ArrayList<Message> messages = new ArrayList<Message>(db.messageDAO().getMessagesByChat(chatId));

        chatName.setText(chat.name);
        itemImage.setImageURI(Uri.parse(requireContext().getFilesDir().getPath() + "/" + chat.image));

        MessageAdapter messageAdapter = new MessageAdapter(requireContext(), messages);

        chatRecyler.setAdapter(messageAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            }
        });


    }
}