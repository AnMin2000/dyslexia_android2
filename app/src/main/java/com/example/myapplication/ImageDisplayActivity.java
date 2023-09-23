package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDisplayActivity extends AppCompatActivity {

    private ImageView imageView;
    private String imageURL = "http://172.16.251.108:8080/getImageURL"; // 이미지 URL 수정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        imageView = findViewById(R.id.imageView);

        // Glide를 사용하여 이미지 로드
        Glide.with(this)
                .load(imageURL)
                .apply(RequestOptions.centerCropTransform())
                .into(imageView);
    }
}
