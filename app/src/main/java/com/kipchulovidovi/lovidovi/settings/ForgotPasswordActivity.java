package com.kipchulovidovi.lovidovi.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kipchulovidovi.lovidovi.R;
import com.kipchulovidovi.lovidovi.auth.CodeVerificationActivity;
import com.kipchulovidovi.lovidovi.auth.LoginActivity;
import com.kipchulovidovi.lovidovi.models.ForgotPasswordModel;
import com.kipchulovidovi.lovidovi.networking.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String RESET = "com.kipchulovidovi.lovidovi.settings";
    TextView backToLogin;
    EditText phoneNumber,enterNewPass,confirmNewPass;
    Button confirmPassChange;
    String newpassword,confirmnew;
    RelativeLayout progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        backToLogin = findViewById(R.id.back_to_login);
        phoneNumber = findViewById(R.id.phone_number);
        enterNewPass = findViewById(R.id.new_password);
        confirmNewPass = findViewById(R.id.confirm_new_password);
        confirmPassChange = findViewById(R.id.change_pass);
        progress = findViewById(R.id.progressLoad);
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        confirmPassChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePass();
            }
        });
    }

    private void changePass() {
        newpassword = enterNewPass.getText().toString();
        confirmnew = confirmNewPass.getText().toString();
        if (!newpassword.equals(confirmnew)){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }else {
            showProgress();
            final String phone = phoneNumber.getText().toString();
            Call<ForgotPasswordModel> call = RetrofitClient.getInstance(this)
                    .getApiConnector()
                    .sendForgotCode(phone);
            call.enqueue(new Callback<ForgotPasswordModel>() {
                @Override
                public void onResponse(Call<ForgotPasswordModel> call, Response<ForgotPasswordModel> response) {
                    hideProgress();
                    if (response.code() == 201) {
                        Intent intent = new Intent(ForgotPasswordActivity.this, CodeVerificationActivity.class);
                        intent.putExtra(RESET, true);
                        intent.putExtra("NUMBER", phone);
                        intent.putExtra("NEWPASS", newpassword);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "response:" + response.message() + response.code(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ForgotPasswordModel> call, Throwable t) {
                    hideProgress();
                    Toast.makeText(ForgotPasswordActivity.this, "Network error. Check your connection.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }
    private void hideProgress(){
        progress.setVisibility(View.GONE);
    }

}
