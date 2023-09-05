package com.example.myapplication;

import com.example.myapplication.dto.Album;
import com.example.myapplication.dto.OcrData;
import com.example.myapplication.dto.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface API {
    // 로그인
    @POST("/insert")
    Call<String> getLoginResponse(@Body User user);

    @Multipart
    @POST("/shot")
    Call<String> getCameraResponse(@Part MultipartBody.Part image);

    @POST("/ocr")
    Call<OcrData> getOcrResponse();


}
