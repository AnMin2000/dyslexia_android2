package com.example.myapplication;

import com.example.myapplication.dto.OcrData;
import com.example.myapplication.dto.Summarize;
import com.example.myapplication.dto.User;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface API {
    // 로그인
    @POST("/insert")
    Call<String> getSignUpResponse(@Body User user);
    @POST("/login")
    Call<Integer> getLoginResponse(@Body User user);
    @POST("/search")
    Call<User> getSearchResponse(@Body User user);

    @Multipart
    @POST("/shot")
    Call<String> getCameraResponse(@Part MultipartBody.Part image);

    @POST("/ocr")
    Call<OcrData> getOcrResponse();

    @POST("/summarize")
    Call<Summarize> sendOcrData(@Body OcrData data);


}
