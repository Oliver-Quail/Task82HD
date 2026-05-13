package com.task82hd.Fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.task82hd.LLMProvider.LLMProvider;
import com.task82hd.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

    LLMProvider llmProvider;

    Uri imageUri;

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

        llmProvider = new LLMProvider();

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        imageUri = uri;
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
                Log.d("ClassifyFragment", "clicked");
                if(!llmProvider.isInitalised()) {
                    Log.d("ClassifyFragment", "initing");
                    llmProvider.ititalise("Meow", imageUri, LLMProvider.PROVIDERS.WEB, getContext());
                }

                Function<String, Void> wrapperFuction = (input) -> {
                  messageResponse(input);
                  return null;
                };

                llmProvider.sendMessage(wrapperFuction);
            }
        });

    }

    private void messageResponse(String message) {
        Log.d("Meow", message);
    }
}