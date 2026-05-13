package com.task82hd.LLMProvider;

import android.net.Uri;

import java.util.ArrayList;
import java.util.function.Function;

public abstract class ILLMProvider {
    protected String message;
    protected Uri image;

    public void initilaise(String message, Uri image) {
        this.message = message;
        this.image = image;
    }

    public abstract void sendMessage(String message, Function<String, Void> callback);

    public abstract void sendMessage(Function<String, Void> callback);

}
