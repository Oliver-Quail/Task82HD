package com.task82hd.LLMProvider;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.task82hd.LLMProvider.Providers.WebProvider;

import java.net.URI;
import java.util.function.Function;

public class LLMProvider {
    String initalMessage;
    Uri image;
    private ILLMProvider provider;
    private PROVIDERS providers;
    Context context;
    private boolean isInitalised = false;


    public boolean isInitalised() {
        return isInitalised;
    }

    public LLMProvider() {

    }

    public void ititalise(String message, Uri image, PROVIDERS providers, Context context) {
        this.initalMessage = message;
        this.image = image;
        this.providers = providers;
        this.context = context;
        isInitalised = true;

        switch (this.providers) {
            case WEB:
                Log.d("LLMProvider", "Inititalised web");
                provider = new WebProvider();
                break;
            default:
                throw new RuntimeException("Invalid provider");
        }
        provider.initilaise(initalMessage, image, context);
    }

    public void sendMessage(String message, Function<String, Void> callback) {
        provider.sendMessage(message, callback);
    }

    public void sendMessage(Function<String, Void> callback) {
        provider.sendMessage(initalMessage, callback);
    }

    public static enum PROVIDERS {
        WEB,
        LOCAL
    }
}
