package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {

    private LinearLayout imageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageContainer = findViewById(R.id.imageContainer);

        // AsyncTask를 사용하여 서버로부터 이미지를 다운로드하고 화면에 표시
        new DownloadImagesTask().execute("http://172.16.49.242:8080/getPhotos");
    }

    private class DownloadImagesTask extends AsyncTask<String, Void, List<Bitmap>> {
        @Override
        protected List<Bitmap> doInBackground(String... urls) {
            String url = urls[0];
            List<Bitmap> bitmapList = new ArrayList<>();

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                StringBuilder response = new StringBuilder();
                int data;
                while ((data = inputStream.read()) != -1) {
                    response.append((char) data);
                }
                JSONArray jsonArray = new JSONArray(response.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    String base64Image = jsonArray.getString(i);
                    byte[] decodedString = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    bitmapList.add(bitmap);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return bitmapList;
        }

        @Override
        protected void onPostExecute(List<Bitmap> bitmapList) {
            for (Bitmap bitmap : bitmapList) {
                if (bitmap != null) {
                    ImageView imageView = new ImageView(ImageActivity.this);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    imageView.setAdjustViewBounds(true);
                    imageView.setImageBitmap(bitmap);
                    imageContainer.addView(imageView);
                }
            }
        }
    }
}