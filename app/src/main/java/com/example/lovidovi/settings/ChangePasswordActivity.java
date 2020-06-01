package com.example.lovidovi.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lovidovi.R;
import com.example.lovidovi.models.ChangedForgotPassModel;
import com.example.lovidovi.networking.RetrofitClient;
import com.example.lovidovi.ui.MainActivity;
import com.example.lovidovi.utils.Constants;
import com.example.lovidovi.utils.SharedPreferencesConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText oldpass,newpass,confirmnewpass;
    Button changePass;
    RelativeLayout progress;
    private String clientsName,clientsUsername,clientsPhone,token;
    private SharedPreferencesConfig sharedPreferencesConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldpass = findViewById(R.id.oldpass);
        newpass = findViewById(R.id.new_password);
        confirmnewpass = findViewById(R.id.confirm_new_password);
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());
        changePass = findViewById(R.id.change_pass);
        progress = findViewById(R.id.progressLoad);
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isErrors()){
                    Toast.makeText(ChangePasswordActivity.this,"Ensure you fill all fields",Toast.LENGTH_SHORT).show();
                }
                if (!isNetworkAvailable()){
                    Toast.makeText(ChangePasswordActivity.this,"Check your network connection",Toast.LENGTH_SHORT).show();
                }
                else {
                    changePassword();
                }
            }
        });
    }

    private void changePassword() {
        String oldPass = oldpass.getText().toString();
        String newPass = newpass.getText().toString();
        String conf = confirmnewpass.getText().toString();
        if (!newPass.equals(conf)){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }else {
            showProgress();
            Call<ChangedForgotPassModel> call = RetrofitClient.getInstance(this)
                    .getApiConnector()
                    .changePassword(newPass, oldPass);
            call.enqueue(new Callback<ChangedForgotPassModel>() {
                @Override
                public void onResponse(Call<ChangedForgotPassModel> call, Response<ChangedForgotPassModel> response) {
                    hideProgress();
                    if (response.code()==201) {
                        Toast.makeText(ChangePasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        token = response.body().getAccessToken();
                        clientsName = Integer.toString(response.body().getUser().getId());
                        clientsUsername = response.body().getUser().getUsername();
                        clientsPhone = response.body().getUser().getPhone();
                        sharedPreferencesConfig.saveAuthenticationInformation(token, clientsName, clientsUsername, clientsPhone, Constants.ACTIVE_CONSTANT);
                        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Your old password is incorrect", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ChangedForgotPassModel> call, Throwable t) {
                    hideProgress();
                    Toast.makeText(ChangePasswordActivity.this, "errot:" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private boolean isErrors(){
        oldpass = findViewById(R.id.oldpass);
        newpass = findViewById(R.id.new_password);
        confirmnewpass = findViewById(R.id.confirm_new_password);
        if (oldpass.getText().toString().isEmpty()){
            return true;
        }if (newpass.getText().toString().isEmpty()){
            return true;
        }if (confirmnewpass.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }
    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }
    private void hideProgress(){
        progress.setVisibility(View.GONE);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
