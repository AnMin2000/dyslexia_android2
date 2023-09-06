package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.dto.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String id = "";
    private String pw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        EditText userId = findViewById(R.id.userId);
        EditText userPassword = findViewById(R.id.userPassword);

        button.setOnClickListener(view -> {
            id = userId.getText().toString();
            pw = userPassword.getText().toString();
            User user = new User();
            user.setId(userId.getText().toString());
            user.setPassword(userPassword.getText().toString());
           // System.out.println(user.getId());
            Log.d("BUTTON CLICKED", "id: " + user.getId() + ", pw: " + user.getPassword());
            login(user);
            Intent intent = new Intent(MainActivity.this,CameraActivity.class); // cameraActivity
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
