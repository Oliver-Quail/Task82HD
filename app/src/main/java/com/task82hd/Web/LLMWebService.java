package com.task82hd.Web;

import com.task82hd.Model.LLMResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface LLMWebService {

    @POST("classify")
    Call<LLMResponse> classify(@Field("message") String message, MultipartBody.Part image);

}
