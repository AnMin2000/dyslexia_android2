package com.example.myapplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    public static API api;

    static {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.17.80:8080/") // 요청 보내는 API 서버 url. /로 끝나야 함
                .addConverterFactory(GsonConverterFactory.create(gson)) // Gson을 역직렬화
                .build();
        api = retrofit.create(API.class);
    }
}