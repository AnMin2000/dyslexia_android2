package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.myapplication.dto.Album;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CODE = 101;
    private Button btn_picture;
    private ImageView imageView;

    private static final int CAMERA_REQUEST_CODE = 1;
    private Uri imageUri;

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

            Bitmap photo = (Bitmap) extras.get("data");
                //System.out.println(photo);
                imageUri = getImageUri(photo);

                // 이미지를 서버로 업로드
                uploadImageToServer(imageUri);
            }
        }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    private void uploadImageToServer(Uri imageUri) {
        try {
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), imageUri.getPath());
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);


            // Retrofit API 인터페이스 생성
            Call<String> call =  RetrofitBuilder.api.getCameraResponse(body);

            // API 호출
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Log.d("Upload", "Image uploaded successfully");
                    } else {
                        Log.e("Upload", "Image upload failed");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Upload", "Image upload failed2", t);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}