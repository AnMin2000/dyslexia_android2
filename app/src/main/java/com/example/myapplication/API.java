package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface API {
    // 로그인
    @POST("/insert")
    Call<String> getLoginResponse(@Body User user);
    //@POST("/insert")
}
