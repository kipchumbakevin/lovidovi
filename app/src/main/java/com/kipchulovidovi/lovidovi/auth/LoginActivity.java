package com.kipchulovidovi.lovidovi.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kipchulovidovi.lovidovi.settings.ForgotPasswordActivity;
import com.kipchulovidovi.lovidovi.ui.MainActivity;
import com.kipchulovidovi.lovidovi.R;
import com.kipchulovidovi.lovidovi.models.UsersModel;
import com.kipchulovidovi.lovidovi.networking.RetrofitClient;
import com.kipchulovidovi.lovidovi.utils.Constants;
import com.kipchulovidovi.lovidovi.utils.SharedPreferencesConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText userName,pass;
    TextView forgotPass;
    Button login,signup;
    private static final String RESETT = "com.kipchulovidovi.lovidovi.auth";
    private String clientsName,clientsUsername,clientsPhone,accessToken,clientsId;
    private SharedPreferencesConfig sharedPreferencesConfig;
    RelativeLayout progressLyt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signup = findViewById(R.id.gotosignup);
        login = findViewById(R.id.button_login);
        forgotPass = findViewById(R.id.forgotPassword);
        userName = findViewById(R.id.logusername);
        progressLyt = findViewById(R.id.progressLoad);
        sharedPreferencesConfig = new SharedPreferencesConfig(LoginActivity.this);
        pass = findViewById(R.id.logpass);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(intent);
                if (userName.getText().toString().isEmpty()){
                    userName.setError("Username required");
                }if (pass.getText().toString().isEmpty()){
                    pass.setError("Password required");
                }else{
                    loginUser();
                    userName.getText().clear();
                    pass.getText().clear();
                }
            }
        });
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("logged", ""+ sharedPreferencesConfig.isloggedIn());

        if (sharedPreferencesConfig.isloggedIn()){
           // Toast.makeText(LoginActivity.this,sharedPreferencesConfig.readClientsId(),Toast.LENGTH_LONG).show();
            welcome();
        }
    }
    private void loginUser() {
        showProgress();
        String username = userName.getText().toString();
        String password = pass.getText().toString();
        Call<UsersModel> call = RetrofitClient.getInstance(LoginActivity.this)
                .getApiConnector()
                .login(username,password);
        call.enqueue(new Callback<UsersModel>() {
            @Override
            public void onResponse(Call<UsersModel> call, Response<UsersModel> response) {
                hideProgress();
                if(response.isSuccessful()){
                  //  String mmm =  Integer.toString(response.body().getUser().getId());
                    accessToken = response.body().getAccessToken();
                    clientsName = Integer.toString(response.body().getUser().getId());
                    clientsUsername = response.body().getUser().getUsername();
                    clientsPhone = response.body().getUser().getPhone();
                 //   clientsId = Integer.toString(response.body().getUser().getId());
                    sharedPreferencesConfig.saveAuthenticationInformation(accessToken,clientsName,clientsUsername,clientsPhone, Constants.ACTIVE_CONSTANT);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(RESETT, true);
                    startActivity(intent);
                    finish();

                }
                else{
                    Toast.makeText(LoginActivity.this,"Server error. Check your details",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<UsersModel> call, Throwable t) {
                hideProgress();
                Toast.makeText(LoginActivity.this,"Network error. Check your connection",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void welcome(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void hideProgress() {
        progressLyt.setVisibility(View.INVISIBLE);
    }

    private void showProgress() {
        progressLyt.setVisibility(View.VISIBLE);
    }

}
