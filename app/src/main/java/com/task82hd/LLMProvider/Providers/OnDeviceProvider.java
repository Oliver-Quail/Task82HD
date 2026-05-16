package com.task82hd.LLMProvider.Providers;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;

import com.google.ai.edge.litertlm.Backend;
import com.google.ai.edge.litertlm.Content;
import com.google.ai.edge.litertlm.Contents;
import com.google.ai.edge.litertlm.Conversation;
import com.google.ai.edge.litertlm.Engine;
import com.google.ai.edge.litertlm.EngineConfig;
import com.google.ai.edge.litertlm.Message;
import com.task82hd.LLMProvider.ILLMProvider;

import java.util.concurrent.Executors;
import java.util.function.Function;

public class OnDeviceProvider extends ILLMProvider {

    Conversation conversation;

    @Override
    public void initilaise(String message, Uri image, Context context) {
        super.initilaise(message, image, context);

        AssetManager assetManager = context.getAssets();

        //assetManager.

        String modelPath = "android.resource://com.task82hd/raw/gemma_4_E2B_it.litertlm";



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

        conversation = engine.createConversation(null);


    }

    @Override
    public void sendMessage(String message, Function<String, Void> callback) {

        Message res = conversation.sendMessage(Contents.Companion.of(
                new Content.ImageFile(image.toString()),
                new Content.Text(message)
        ), null);
    }

    @Override
    public void sendMessage(Function<String, Void> callback) {
        this.sendMessage(this.message, callback);
    }
}
