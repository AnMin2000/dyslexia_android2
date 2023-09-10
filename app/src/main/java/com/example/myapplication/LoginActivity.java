package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myapplication.dto.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class LoginActivity extends AppCompatActivity {

    private String id = "";
    private String pw = "";
    EditText userId, userPassword, SignUpView, SearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button button = findViewById(R.id.button);
        userId = findViewById(R.id.userId);
        userPassword = findViewById(R.id.userPassword);
        SignUpView = findViewById(R.id.SignUpView);
        SearchView = findViewById(R.id.SearchView);


        SignUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class); // cameraActivity
                startActivity(intent);
            }
        });

        // loginbutton 클릭 시 입력 막기
        SearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SearchActivity.class); // cameraActivity
                startActivity(intent);
            }
        });

        button.setOnClickListener(view -> {
            id = userId.getText().toString();
            pw = userPassword.getText().toString();
            User user = new User();
            user.setId(userId.getText().toString());
            user.setPassword(userPassword.getText().toString());
           // System.out.println(user.getId());
            Log.d("BUTTON CLICKED", "id: " + user.getId() + ", pw: " + user.getPassword());
            login(user);
            Intent intent = new Intent(LoginActivity.this,CameraActivity.class); // cameraActivity
            startActivity(intent);
        });
    }

    private void login(User user) {
        Call<String> call = RetrofitBuilder.api.getLoginResponse(user);
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
