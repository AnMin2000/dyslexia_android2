package com.example.myapplication;

import com.example.myapplication.dto.Album;
import com.example.myapplication.dto.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface API {
    // 로그인
    @POST("/insert")
    Call<String> getLoginResponse(@Body User user);
    @POST("/shot")
    Call<String> getCameraResponse(@Body Album album);

}
