package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

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

    private List<ImageView> imageViews = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        // 이미지를 표시할 ImageView들을 리스트에 추가
        imageViews.add((ImageView) findViewById(R.id.imageView1));
        imageViews.add((ImageView) findViewById(R.id.imageView2));
        // 필요한 만큼 ImageView 추가

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
            for (int i = 0; i < Math.min(imageViews.size(), bitmapList.size()); i++) {
                ImageView imageView = imageViews.get(i);
                Bitmap bitmap = bitmapList.get(i);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
