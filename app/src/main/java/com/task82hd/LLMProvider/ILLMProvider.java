package com.task82hd.LLMProvider;

import android.net.Uri;

import java.util.ArrayList;
import java.util.function.Function;

public interface ILLMProvider {

    public void initilaise(String message, Uri image);

    public void sendMessage(String message, Function<String, Void> callback);

    public void sendMessage(Function<String, Void> callback);

}
