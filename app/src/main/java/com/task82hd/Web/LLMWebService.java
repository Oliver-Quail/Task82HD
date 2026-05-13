package com.task82hd.Web;

import com.task82hd.Model.LLMResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface LLMWebService {

    @Multipart
    @POST("classify")
    Call<LLMResponse> classify(@Part MultipartBody.Part image, @Part("imageData") RequestBody imageData);

}
