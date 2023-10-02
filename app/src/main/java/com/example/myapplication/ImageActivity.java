package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

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

    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        tableLayout = findViewById(R.id.tableLayout3);

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
            int numColumns = 2; // 열 수
            int rowCount = 0; // 현재 행 수

            for (Bitmap bitmap : bitmapList) {
                if (bitmap != null) {
                    if (rowCount % numColumns == 0) {
                        // 새로운 행을 추가
                        TableRow currentRow = new TableRow(ImageActivity.this);
                        tableLayout.addView(currentRow);

                        // TableRow의 크기를 설정
                        currentRow.setLayoutParams(new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                500)); // TableRow의 높이를 145dp로 고정
                    }

                    // ImageView를 생성하고 추가
                    ImageView imageView = new ImageView(ImageActivity.this);

                    // ImageView의 크기를 설정
                    imageView.setLayoutParams(new TableRow.LayoutParams(
                            250, // TableRow의 가로를 194dp로 고정
                            280)); // TableRow의 높이를 145dp로 고정
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    imageView.setAdjustViewBounds(true);
                    imageView.setImageBitmap(bitmap);

                    // 현재 행에 ImageView 추가
                    TableRow currentRow = (TableRow) tableLayout.getChildAt(rowCount / numColumns);
                    currentRow.addView(imageView);
                    rowCount++;
                }
            }
        }
    }
}
