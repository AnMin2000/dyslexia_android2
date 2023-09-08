package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.dto.OcrData;
import com.example.myapplication.dto.Summarize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OcrActivity extends AppCompatActivity {

    Bitmap image; //사용되는 이미지
    TextView OCRTextView, summarizeTextView; // OCR 결과뷰
    ImageView imageView;
    Button OCRButton, summarizeButton, soundButton;

    OcrData data;
    Summarize data2;

    TextToSpeech textToSpeech;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);


        imageView = findViewById(R.id.imageView);
        OCRButton = findViewById(R.id.OCRButton);
        OCRTextView = findViewById(R.id.OCRTextView);
        summarizeTextView = findViewById(R.id.summarizeTextView);
        summarizeButton = findViewById(R.id.summarizeButton);
        soundButton = findViewById(R.id.soundButton);

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
        summarizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Summarize> call2 = RetrofitBuilder.api.sendOcrData(data);

                call2.enqueue(new Callback<Summarize>() {
                    @Override
                    public void onResponse(Call<Summarize> call, Response<Summarize> response) {
                        if (response.isSuccessful()) {
                            data2 = response.body();
                            String name = data2.getData2();

                            try {
                                JSONObject jsonObject = new JSONObject(name);

                                // "choices" 필드가 존재하는지 확인
                                if (jsonObject.has("choices")) {
                                    JSONArray choices = jsonObject.getJSONArray("choices");
                                    if (choices.length() > 0) {
                                        JSONObject choice = choices.getJSONObject(0);
                                        JSONObject message = choice.getJSONObject("message");
                                        String content = message.getString("content");

                                        // "content"를 출력하거나 다른 작업 수행
                                        System.out.println(content);

                                        // 여기에서 content 변수에 있는 텍스트만 추출됩니다.
                                        String extractedText = content;

                                        // 추출된 텍스트를 다른 곳에 사용하려면 이어서 작업을 수행하세요.
                                        // 예를 들어, summarizeTextView에 설정하려면 다음과 같이 사용할 수 있습니다.
                                         summarizeTextView.setText(extractedText);
                                    }
                                } else {
                                    Log.e("test", "No 'choices' field in the JSON response");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("test", "실패 1");
                        }
                    }

                    @Override
                    public void onFailure(Call<Summarize> call, Throwable t) {
                        Log.e("test", "실패 2", t);
                    }
                });
            }
        });
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int langResult = textToSpeech.setLanguage(Locale.KOREA);

                    if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // 언어 데이터가 없거나 지원되지 않는 경우 처리
                        // 필요에 따라 에러 처리를 추가하세요.
                    }
                }
            }
        });
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textToSpeech != null) {
                    String textToRead = summarizeTextView.getText().toString();
                    textToSpeech.speak(textToRead, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });

    }
    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
    private void uploadOcrToServer() {
        try {

            Call<OcrData> call = RetrofitBuilder.api.getOcrResponse();

            call.enqueue(new Callback<OcrData>() {
                @Override
                public void onResponse(Call<OcrData> call, Response<OcrData> response) {
                    if (response.isSuccessful()) {
                        data = response.body();
                        String name = data.getData();
                        OCRTextView.setText(name);
                    } else {
                        Log.e("Ocr", "Ocr 업로드 실패");
                    }
                }

                @Override
                public void onFailure(Call<OcrData> call, Throwable t) {
                    Log.e("Ocr", "Ocr 업로드 실패", t);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}