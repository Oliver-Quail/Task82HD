package com.task82hd.Fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.task82hd.Adapter.MessageAdapter;
import com.task82hd.Database.AppDatabase;
import com.task82hd.Database.Entity.Message;
import com.task82hd.Database.Entity.Misc;
import com.task82hd.LLMProvider.LLMProvider;
import com.task82hd.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.function.Function;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSource;
import okio.Okio;

public class ClassifyFragment extends Fragment {

    ConstraintLayout addImage;
    Button classifyButton;
    TextInputEditText informationText;
    ProgressBar loadingBar;
    TextView statusText;

    LLMProvider llmProvider;

    RecyclerView chatRecycler;

    ImageView userImage;

    ArrayList<Message> messages;

    Uri imageUri;
    String imageName;
    String messageText;

    public ClassifyFragment() {
        // Required empty public constructor
    }

    public static ClassifyFragment newInstance(String param1, String param2) {
        ClassifyFragment fragment = new ClassifyFragment();
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
        return inflater.inflate(R.layout.fragment_classify, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addImage = view.findViewById(R.id.add_image);
        classifyButton = view.findViewById(R.id.classify_button);
        informationText = view.findViewById(R.id.information_text);
        chatRecycler = view.findViewById(R.id.chat_recycler);
        loadingBar = view.findViewById(R.id.loading_bar);
        userImage = view.findViewById(R.id.user_image);
        statusText = view.findViewById(R.id.status_text);
        loadingBar.setVisibility(View.INVISIBLE);
        statusText.setVisibility(View.INVISIBLE);
        llmProvider = new LLMProvider();

        messages = new ArrayList<>();

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {

                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        imageUri = uri;
                        saveFileToInternalStorage(imageUri);
                        userImage.setImageURI(imageUri);


                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        classifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppDatabase db = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();
                Misc misc = db.miscDAO().getMisc();
                LLMProvider.PROVIDERS provider = LLMProvider.PROVIDERS.values()[misc.mode];

                statusText.setVisibility(View.VISIBLE);
                loadingBar.setVisibility(View.VISIBLE);
                statusText.setText("Initialising...");
                Message message = new Message();
                message.setContents(informationText.getText().toString());
                message.setAi(false);
                messages.add(message);

                updateChat();
                Log.d("ClassifyFragment", "it");

                Handler handler = new Handler(Looper.getMainLooper());

                if(provider == LLMProvider.PROVIDERS.WEB) {
                    if(!llmProvider.isInitalised()) {
                        prepLLM(LLMProvider.PROVIDERS.WEB);
                    }
                    loadingBar.setVisibility(View.VISIBLE);
                    statusText.setText("Thinking...");
                    Function<String, Void> wrapperFuction = (input) -> {
                        messageResponse(input);
                        cleanUI();
                        return null;
                    };
                    llmProvider.sendMessage(wrapperFuction);
                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(!llmProvider.isInitalised()) {
                                prepLLM(LLMProvider.PROVIDERS.LOCAL);
                            }
                            loadingBar.setVisibility(View.VISIBLE);
                            statusText.setText("Thinking...");
                            Function<String, Void> wrapperFuction = (input) -> {
                                messageResponse(input);
                                return null;
                            };
                            llmProvider.sendMessage(wrapperFuction);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    cleanUI();
                                }
                            });
                        }
                    }).start();
                }
            }
        });

    }

    private void prepLLM(LLMProvider.PROVIDERS provider) {
        loadingBar.setVisibility(View.VISIBLE);
        statusText.setVisibility(View.VISIBLE);
        statusText.setText("Initialising...");
        llmProvider.ititalise(informationText.getText().toString(), imageUri, provider, getContext(), imageName);
    }
    private void cleanUI() {
        loadingBar.setVisibility(View.INVISIBLE);
        statusText.setVisibility(View.INVISIBLE);
        Message newMessage = new Message();
        newMessage.setContents(messageText);
        newMessage.setAi(true);
        messages.add(newMessage);
        updateChat();
    }

    private void messageResponse(String message) {
        this.messageText = message;

    }

    private void updateChat() {
        MessageAdapter messageAdapter = new MessageAdapter(requireContext(), messages);
        chatRecycler.setAdapter(messageAdapter);
    }

    private Uri saveFileToInternalStorage(Uri uri) {
        File destinationFile = null;


        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            imageName = "picked_media_" + System.currentTimeMillis() + ".jpg";
            destinationFile = new File(requireContext().getFilesDir(), imageName);

            try (OutputStream outputStream = new FileOutputStream(destinationFile)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
            }
            inputStream.close();
            Log.d("SaveFile", "File saved at: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.parse(destinationFile.toURI().toString());
    }
}