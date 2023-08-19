package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.myapplication.dto.Album;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraActivity extends AppCompatActivity {

    private Button btn_picture;
    private ImageView imageView;

    private static final int REQUEST_IMAGE_CODE = 101;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.imageView);

        btn_picture = findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    //사진찍기
    public void takePicture(){

        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(imageTakeIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CODE);
        }
    }

    //결과값 가져오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();

            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Album album = new Album();
            album.setAlbumId(imageBitmap.toString());
            camera(album);
           // imageView.setImageBitmap(imageBitmap); 앨범에 사진을 표시 하는 코드
        }
    }
    private void camera(Album album){
        Call<String> call = RetrofitBuilder.api.getCameraResponse(album);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println(""+response);
                if (response.isSuccessful()) {
                    Log.d("RESPONSE: ", response.body());
                } else {
                    Log.d("RESPONSE", "FAILURE");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("CONNECTION FAILURE: ", t.getLocalizedMessage());
            }
        });
    }
}