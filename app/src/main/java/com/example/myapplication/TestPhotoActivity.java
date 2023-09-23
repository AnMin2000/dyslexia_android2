package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.dto.Summarize;
import com.squareup.picasso.Picasso;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class TestPhotoActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_photo);

        imageView = findViewById(R.id.imageView);


        Call<ResponseBody> call = RetrofitBuilder.api.getImage();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // InputStream을 파일로 저장
                    try {
                        InputStream inputStream = response.body().byteStream();
                        File imageFile = saveInputStreamToFile(inputStream);

                        // Picasso를 사용하여 이미지를 표시
                        Picasso.get()
                                .load(imageFile)
                                .into(imageView);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // InputStream을 파일로 저장하는 메서드
    private File saveInputStreamToFile(InputStream inputStream) throws IOException {
        File outputDir = getCacheDir();
        File outputFile = File.createTempFile("image", "jpg", outputDir);
        FileOutputStream outputStream = new FileOutputStream(outputFile);

        byte[] buffer = new byte[4 * 1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        outputStream.close();

        return outputFile;
    }
}