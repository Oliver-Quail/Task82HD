package com.task82hd.LLMProvider;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;
import java.util.function.Function;

public abstract class ILLMProvider {
    protected String message;
    protected Uri image;
    protected Context context;

    public void initilaise(String message, Uri image, Context context) {
        this.message = message;
        this.image = image;
        this.context = context;
    }

    public abstract void sendMessage(String message, Function<String, Void> callback);

    public abstract void sendMessage(Function<String, Void> callback);

}
