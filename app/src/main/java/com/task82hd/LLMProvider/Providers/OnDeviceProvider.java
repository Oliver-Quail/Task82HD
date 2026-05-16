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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class OnDeviceProvider extends ILLMProvider {

    Conversation conversation;

    @Override
    public void initilaise(String message, Uri image, Context context) {
        super.initilaise(message, image, context);

        AssetManager assetManager = context.getAssets();

        //assetManager.
        String modelName = "gemma_4_e2b_it.litertlm";

        String[] directoryToCheck = context.fileList();

        Log.d("DeviceProvider", directoryToCheck.toString());



        if(!Arrays.asList(directoryToCheck).contains("A")) {
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
                null,
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

        Message res = conversation.sendMessage(Contents.Companion.of(
                //new Content.ImageFile(image.toString()),
                new Content.Text(message)
        ), extraContent);

        callback.apply(res.getContents().toString());

        Log.d("OnDeviceLLMProvider", res.getContents().toString());
    }

    @Override
    public void sendMessage(Function<String, Void> callback) {
        this.sendMessage(this.message, callback);
    }
}
