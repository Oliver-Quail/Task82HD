package com.task82hd.LLMProvider.Providers;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.Log;

import com.google.ai.edge.litertlm.Backend;
import com.google.ai.edge.litertlm.Content;
import com.google.ai.edge.litertlm.Contents;
import com.google.ai.edge.litertlm.Conversation;
import com.google.ai.edge.litertlm.ConversationConfig;
import com.google.ai.edge.litertlm.Engine;
import com.google.ai.edge.litertlm.EngineConfig;
import com.google.ai.edge.litertlm.Message;
import com.google.ai.edge.litertlm.Tool;
import com.google.ai.edge.litertlm.ToolProvider;
import com.task82hd.LLMProvider.ILLMProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class OnDeviceProvider extends ILLMProvider {

    Conversation conversation;
    String systemPrompt;
    String dir;

    @Override
    public void initilaise(String message, Uri image, Context context) {
        super.initilaise(message, image, context);

        AssetManager assetManager = context.getAssets();

        dir = context.getFilesDir().getPath();

        Log.d("aaa", dir);
        //assetManager.
        String modelName = "gemma_4_e2b_it.litertlm";

        String[] directoryToCheck = context.fileList();

        try {
            InputStream systemPromptStream = assetManager.open("system_prompt.txt");
            byte[] buffer = new byte[1024];
            systemPromptStream.read(buffer);
            systemPrompt = new String(buffer, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        if(!Arrays.asList(directoryToCheck).contains("gemma_4_e2b_it.litertlm")) {
            try {
                InputStream model = assetManager.open(modelName);

                File out = new File(context.getFilesDir(), modelName);

                OutputStream transferModel = new FileOutputStream(out);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = model.read(buffer)) != -1) {
                    transferModel.write(buffer, 0, read);
                }

                transferModel.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        String modelPath = context.getFilesDir().getPath() + "/" + modelName;



        EngineConfig config = new EngineConfig(
                modelPath,
                new Backend.CPU(),
                new Backend.CPU(),
                null,
                null,
                null,
                null
        );

        Engine engine = new Engine(config);

        engine.initialize();

        List<ToolProvider> tools = List.of();
        List<Message> messages = List.of();
        Map<String, String> extraContent = Map.of();

        ConversationConfig conversationConfig = new ConversationConfig(
                Contents.Companion.of(

                ),
                messages,
                tools,
                null,
                false,
                null,
                extraContent
        );


        conversation = engine.createConversation(conversationConfig);


    }

    @Override
    public void sendMessage(String message, Function<String, Void> callback) {
        Map<String, String> extraContent = Map.of();

        File temp = new File(context.getFilesDir(), "picked_media_1778919930173.jpg");

        String fileName = image.getPath().substring(image.getPath().lastIndexOf('/') + 1);

        String imageLocation = context.getFilesDir().getPath() + "/picked_media_1778919930173.jpg" ;
        File imageBinary = context.getFileStreamPath(fileName);

        String[] directory = context.fileList();

        Log.d("aaa", Arrays.asList(directory).toString());

        Log.d("aaa", fileName);
        Log.d("aaa", String.valueOf(imageBinary.exists()));
        Log.d("aaa", image.toString());
        Log.d("aaa", image.toString());
        Log.d("aaa", String.valueOf(imageBinary.length()));



        if(temp.exists()) {
            Log.d("OnDeviceProvider", "file exists");
        }
        try {


            Message res = conversation.sendMessage(Contents.Companion.of(
                    new Content.ImageBytes(Files.readAllBytes(Paths.get(imageBinary.getAbsolutePath()))),
                    new Content.Text(systemPrompt + message)
            ), extraContent);
            callback.apply(res.getContents().toString());

            Log.d("OnDeviceLLMProvider", res.getContents().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void sendMessage(Function<String, Void> callback) {
        this.sendMessage(this.message, callback);
    }
}
