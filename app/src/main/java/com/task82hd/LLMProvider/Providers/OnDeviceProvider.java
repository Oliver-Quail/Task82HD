package com.task82hd.LLMProvider.Providers;

import android.content.Context;
import android.net.Uri;

import com.task82hd.LLMProvider.ILLMProvider;

import java.util.function.Function;

public class OnDeviceProvider extends ILLMProvider {

    @Override
    public void initilaise(String message, Uri image, Context context) {
        super.initilaise(message, image, context);

        String modelPath = "";

    }

    @Override
    public void sendMessage(String message, Function<String, Void> callback) {

    }

    @Override
    public void sendMessage(Function<String, Void> callback) {

    }
}
