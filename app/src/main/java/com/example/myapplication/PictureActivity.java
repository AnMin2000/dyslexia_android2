package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ScrollView;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.view.ViewGroup.LayoutParams;
import android.graphics.drawable.GradientDrawable;

public class PictureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        TableLayout tableLayout = findViewById(R.id.tableLayout);

        // 여기서 이미지 파일 이름을 변경하여 이미지를 추가하세요.
        String[] imageFileNames = {"a8", "a8", "a8", "a8","a8", "a8", "a8", "a8","a8", "a8", "a8", "a8"};

        int columnCount = 2; // 열 수
        int rowCount = (int) Math.ceil((double) imageFileNames.length / columnCount);

        Context context = this;

        for (int i = 0; i < rowCount; i++) {
            TableRow tableRow = new TableRow(context);

            for (int j = 0; j < columnCount; j++) {
                int index = i * columnCount + j;
                if (index < imageFileNames.length) {
                    ImageView imageView = new ImageView(context);
                    int imageResource = getResources().getIdentifier(imageFileNames[index], "drawable", getPackageName());
                    imageView.setImageResource(imageResource);

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

