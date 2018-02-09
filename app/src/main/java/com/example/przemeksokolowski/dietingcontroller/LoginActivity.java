package com.example.przemeksokolowski.dietingcontroller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.przemeksokolowski.dietingcontroller.data.ApiUtils;
import com.example.przemeksokolowski.dietingcontroller.model.LoginData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText loginField = findViewById(R.id.login_field);
        final EditText passwordField = findViewById(R.id.password_field);
        Button loginBtn = findViewById(R.id.login_button);

        if (loginField.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                ApiUtils.getWebApi().login(login, password).enqueue(new Callback<LoginData>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginData> call, @NonNull Response<LoginData> response) {
                        if (response.isSuccessful()) {
//                            int userId = response.body().getId();
                            int userId = 1;
                            if (userId == -1) {
                                loginField.setError("Niepoprawne dane!");
                                passwordField.setError("Niepoprawne dane!");
                            } else {
                                startActivity(ApiUtils.createIntentWithLoggedUserId(
                                        LoginActivity.this, MainActivity.class, userId));
                            }

                            Log.i("loginAttempt", "POST submitted to API." + response.body().toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginData> call, @NonNull Throwable t) {
                        Log.e("loginAttempt", t.getMessage());
                        Log.e("loginAttempt", "Unable to submit post to API.");
                        ApiUtils.noApiConnectionDialog(LoginActivity.this);
                    }
                });
            }
        });

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}