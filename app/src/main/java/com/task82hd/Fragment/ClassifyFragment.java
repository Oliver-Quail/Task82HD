package com.task82hd.Fragment;

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

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputEditText;
import com.task82hd.Adapter.MessageAdapter;
import com.task82hd.Database.Entity.Message;
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

    LLMProvider llmProvider;

    RecyclerView chatRecycler;

    ImageView userImage;

    ArrayList<Message> messages;

    Uri imageUri;
    String imageName;

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

        llmProvider = new LLMProvider();

        messages = new ArrayList<>();

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {

                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        imageUri = uri;
                        Uri imageSelected = saveFileToInternalStorage(imageUri);

                        Context context = requireContext();


                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.DISPLAY_NAME, "image_" + System.currentTimeMillis() + ".jpg");
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp");


                        Uri newUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        requireContext().getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        try {
                            OutputStream out = context.getContentResolver().openOutputStream(newUri);
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageSelected);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                out.close();
                                imageUri = newUri;
                                userImage.setImageURI(newUri);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }


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
                    Message message = new Message();
                    message.setContents(informationText.getText().toString());
                    message.setAi(false);
                    messages.add(message);
                    updateChat();
                    loadingBar.setVisibility(View.VISIBLE);
                    Log.d("ClassifyFragment", "initing");
                    llmProvider.ititalise(informationText.getText().toString(), imageUri, LLMProvider.PROVIDERS.LOCAL, getContext(), imageName);
                }

                Function<String, Void> wrapperFuction = (input) -> {
                    loadingBar.setVisibility(View.INVISIBLE);
                  messageResponse(input);
                  return null;
                };

                llmProvider.sendMessage(wrapperFuction);
            }
        });

    }

    private void messageResponse(String message) {
        Message newMessage = new Message();
        newMessage.setContents(message);
        newMessage.setAi(true);
        messages.add(newMessage);
        updateChat();

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