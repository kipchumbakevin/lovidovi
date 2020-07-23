package com.kipchulovidovi.lovidovi.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CCPCountry;
import com.kipchulovidovi.lovidovi.R;
import com.kipchulovidovi.lovidovi.models.SignUpMessagesModel;
import com.kipchulovidovi.lovidovi.networking.RetrofitClient;
import com.kipchulovidovi.lovidovi.utils.SharedPreferencesConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    EditText name,username,phone,password,confirm,phone_input;
    Button signup,login;
    CheckBox checkbox;
    TextView policy;
    SharedPreferencesConfig sharedPreferencesConfig;
    RelativeLayout progressLyt;
    CCPCountry ccp = new CCPCountry();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signup = findViewById(R.id.button_sign);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        policy = findViewById(R.id.policy);
        phone_input = findViewById(R.id.phone_input);
        checkbox = findViewById(R.id.checkbox);
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
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.lovidovi.co.ke/policy");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.lovidovi.co.ke/policy")));
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkbox.isChecked()){
                    Toast.makeText(SignUpActivity.this,"To continue, you have to agree to our privacy policy",Toast.LENGTH_SHORT).show();
                }
                if (isErrors()){
                    Toast.makeText(SignUpActivity.this,"Ensure you fill all fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    signupUser();
                }
            }
        });
    }

    private boolean isErrors() {
        if (name.getText().toString().isEmpty()){
            return true;
        }if (username.getText().toString().isEmpty()){
            return true;
        }if (phone.getText().toString().isEmpty()){
            return true;
        }if (password.getText().toString().isEmpty()){
            return true;
        }if (confirm.getText().toString().isEmpty()){
            return true;
        }else{
            return false;
        }
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
                    } else {
                        Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
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
