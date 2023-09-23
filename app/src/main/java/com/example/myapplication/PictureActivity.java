package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ScrollView;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.view.ViewGroup.LayoutParams;
import android.graphics.drawable.GradientDrawable;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class PictureActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        // 외부 저장소 읽기 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                return;
            }
        }

        TableLayout tableLayout = findViewById(R.id.tableLayout);

        // 외부 저장소에서 이미지 파일을 읽어올 디렉토리 경로 설정
        String externalStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        String albumDirectory = externalStorageDirectory + "/Album/JPEG_20230911";

        File albumDir = new File(albumDirectory);
        if (!albumDir.exists() || !albumDir.isDirectory()) {
            // 앨범 디렉토리가 존재하지 않는 경우 처리
            Log.e("PictureActivity", "앨범 디렉토리가 존재하지 않습니다.");
            Toast.makeText(this, "앨범 디렉토리가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 앨범 디렉토리에서 파일 목록 가져오기
        File[] imageFiles = albumDir.listFiles();

        int columnCount = 2; // 열 수
        int rowCount = (int) Math.ceil((double) imageFiles.length / columnCount);

        Context context = this;

        for (int i = 0; i < rowCount; i++) {
            TableRow tableRow = new TableRow(context);

            for (int j = 0; j < columnCount; j++) {
                int index = i * columnCount + j;
                if (index < imageFiles.length) {
                    ImageView imageView = new ImageView(context);
                    imageView.setImageURI(Uri.fromFile(imageFiles[index]));

                    // TableRow의 높이를 동적으로 조절
                    int tableRowHeight = getResources().getDimensionPixelSize(R.dimen.image_height); // 세로 길이 145dp
                    TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            tableRowHeight
                    );
                    tableRow.setLayoutParams(rowParams);

                    // ImageView의 크기를 조절
                    int imageWidth = getResources().getDimensionPixelSize(R.dimen.image_width); // 가로 길이 194dp
                    int imageHeight = getResources().getDimensionPixelSize(R.dimen.image_height); // 세로 길이 145dp
                    TableRow.LayoutParams params = new TableRow.LayoutParams(
                            imageWidth,
                            imageHeight
                    );
                    imageView.setLayoutParams(params);

                    tableRow.addView(imageView);
                }
            }

            tableLayout.addView(tableRow);
        }
    }
}
