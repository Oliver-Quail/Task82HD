package com.task82hd.LLMProvider;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.room.Room;

import com.task82hd.Database.AppDatabase;
import com.task82hd.Database.Entity.Chat;
import com.task82hd.Database.Entity.Message;
import com.task82hd.LLMProvider.Providers.WebProvider;

import java.net.URI;
import java.util.function.Function;

public class LLMProvider {
    String initalMessage;
    Uri image;
    ILLMProvider provider;
    PROVIDERS providers;
    Context context;
    boolean isInitalised = false;

    AppDatabase db;
    long chatId;

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

        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();
        Chat chat = new Chat();
        chat.image = image.toString();
        chat.name = "Unclassified";
        chatId = db.chatDAO().createChat(chat);

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
        Message userMessage = new Message();
        userMessage.chatId = (int)chatId;
        userMessage.isAi = false;
        userMessage.contents = message;
        db.messageDAO().createMessage(userMessage);


        Function<String, Void> logingCallback = (input) -> {
            Message aiMessage = new Message();
            aiMessage.chatId = (int)chatId;
            aiMessage.isAi = true;
            aiMessage.contents = input;
            db.messageDAO().createMessage(aiMessage);

            callback.apply(input);

            return null;
        };

        provider.sendMessage(message, logingCallback);

    }

    public void sendMessage(Function<String, Void> callback) {
        this.sendMessage(initalMessage, callback);
    }

    public static enum PROVIDERS {
        WEB,
        LOCAL
    }
}
