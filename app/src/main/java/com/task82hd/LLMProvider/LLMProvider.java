package com.task82hd.LLMProvider;

import android.content.Context;
import android.net.Uri;

import com.task82hd.LLMProvider.Providers.WebProvider;

import java.net.URI;
import java.util.function.Function;

public class LLMProvider {
    String initalMessage;
    Uri image;
    private ILLMProvider provider;
    private PROVIDERS providers;
    private boolean canExecute = false;
    Context context;

    public LLMProvider(String message, Uri image, PROVIDERS providers, Context context) {
        this.initalMessage = message;
        this.image = image;
        this.providers = providers;
        this.context = context;
    }

    public void ititalise() {

        switch (providers) {
            case WEB:
                provider = new WebProvider();
                canExecute = true;
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
        provider.sendMessage(callback);
    }

    public static enum PROVIDERS {
        WEB,
        LOCAL
    }
}
