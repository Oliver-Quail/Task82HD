package com.task82hd.LLMProvider.Providers;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.task82hd.LLMProvider.ILLMProvider;
import com.task82hd.Model.LLMResponse;
import com.task82hd.Web.LLMWebService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebProvider extends ILLMProvider {
    MultipartBody.Part file;

    @Override
    public void initilaise(String message, Uri image, Context context) {
        super.initilaise(message, image, context);

        if(this.image == null)
            return;

        try {
            InputStream inputStream = this.context.getContentResolver().openInputStream(image);
            BufferedSource source = Okio.buffer(Okio.source(inputStream));
            byte[] bytes = source.readByteArray();
            source.close();


            RequestBody requestFile = RequestBody.create(
                    MediaType.parse(this.context.getContentResolver().getType(image)),
                    bytes
            );
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", "upload.jpg", requestFile);
            file = body;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


        @Override
    public void sendMessage(String message, Function<String, Void> callback) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.50.179:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(90, TimeUnit.SECONDS).build()).build();


            LLMWebService service = retrofit.create(LLMWebService.class);
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), message);

            Call<LLMResponse> result = service.classify(file, description);

            result.enqueue(new Callback<LLMResponse>() {
                @Override
                public void onResponse(Call<LLMResponse> call, Response<LLMResponse> response) {

                    LLMResponse llmResponse = response.body();
                    Log.d("WebProvider", "response Recieved");
                    String processedResponse = llmResponse.getName() + "::::" + llmResponse.getClassification();
                    callback.apply(processedResponse);

                }

                @Override
                public void onFailure(Call<LLMResponse> call, Throwable t) {
                    Log.d("WebProvider", "response failed");

                    callback.apply("Unable to reach server. Check your connection");


                }
            });


    }

    @Override
    public void sendMessage(Function<String, Void> callback) {
        this.sendMessage(message, callback);
    }
}
