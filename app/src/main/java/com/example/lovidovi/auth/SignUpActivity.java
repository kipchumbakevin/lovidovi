package com.example.lovidovi.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lovidovi.R;
import com.example.lovidovi.models.SignUpMessagesModel;
import com.example.lovidovi.networking.RetrofitClient;
import com.example.lovidovi.utils.SharedPreferencesConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    EditText name,username,phone,password,confirm;
    Button signup,login;
    SharedPreferencesConfig sharedPreferencesConfig;
    RelativeLayout progressLyt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signup = findViewById(R.id.button_sign);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        progressLyt = findViewById(R.id.progressLoad);
        login = findViewById(R.id.login);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.pass);
        confirm = findViewById(R.id.confirmpass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupUser();
            }
        });
    }

    private void signupUser() {
        final String nameN,userN,phoneN,passN,confirmPassN;
        nameN = name.getText().toString();
        userN = username.getText().toString();
        phoneN = phone.getText().toString();
        passN = password.getText().toString();
        confirmPassN = confirm.getText().toString();
        if (!passN.equals(confirmPassN)){
            Toast.makeText(SignUpActivity.this,"Passwords do not match",Toast.LENGTH_LONG).show();
        }else {
            showProgress();
            Call<SignUpMessagesModel> call = RetrofitClient.getInstance(SignUpActivity.this)
                    .getApiConnector()
                    .checkIfExist(phoneN, userN);
            call.enqueue(new Callback<SignUpMessagesModel>() {
                @Override
                public void onResponse(Call<SignUpMessagesModel> call, Response<SignUpMessagesModel> response) {
                    hideProgress();
                    if (response.code() == 201) {
                        Intent intent = new Intent(SignUpActivity.this, CodeVerificationActivity.class);
                        intent.putExtra("NAME",nameN);
                        intent.putExtra("USER", userN);
                        intent.putExtra("NUMBER", phoneN);
                        intent.putExtra("PASS", passN);
                        intent.putExtra("CONFIRM", confirmPassN);
                        startActivity(intent);
                        finish();
                        Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Server error", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<SignUpMessagesModel> call, Throwable t) {
                    hideProgress();
                    Toast.makeText(SignUpActivity.this, "Network error. Check your connection.", Toast.LENGTH_LONG).show();
                }
            });
        }

    }
    private void hideProgress() {
        progressLyt.setVisibility(View.INVISIBLE);
    }

    private void showProgress() {
        progressLyt.setVisibility(View.VISIBLE);
    }
}
