package com.task82hd.LLMProvider.Providers;

import android.net.Uri;

import com.task82hd.LLMProvider.ILLMProvider;

import java.util.function.Function;

public class WebProvider extends ILLMProvider {
    @Override
    public void initilaise(String message, Uri image) {
        super.initilaise(message, image);


    }

    @Override
    public void sendMessage(String message, Function<String, Void> callback) {

    }

    @Override
    public void sendMessage(Function<String, Void> callback) {

    }
}
