package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OcrActivity extends AppCompatActivity {

    Bitmap image; //사용되는 이미지
    TextView OCRTextView; // OCR 결과뷰
    ImageView imageView;
    Button OCRButton;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);


        imageView = findViewById(R.id.imageView);
        OCRButton = findViewById(R.id.OCRButton);
        OCRTextView = findViewById(R.id.OCRTextView);

        //이미지 디코딩을 위한 초기화
        byte[] byteArray = getIntent().getByteArrayExtra("image");

        image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imageView.setImageBitmap(image);

        OCRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadOcrToServer();
            }
        });


    }
    private void uploadOcrToServer() {
        try {

            Call<String> call = RetrofitBuilder.api.getOcrResponse();

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        String ocrResult = response.body();
                        Log.d("Ocr", response.body());
                        OCRTextView.setText(ocrResult);
                    } else {
                        Log.e("Ocr", "Ocr 업로드 실패");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Ocr", "Ocr 업로드 실패", t);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}