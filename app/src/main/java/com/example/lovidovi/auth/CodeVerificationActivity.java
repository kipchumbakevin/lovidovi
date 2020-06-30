package com.example.lovidovi.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovidovi.models.ChangedForgotPassModel;
import com.example.lovidovi.ui.MainActivity;
import com.example.lovidovi.R;
import com.example.lovidovi.models.SendCodeModel;
import com.example.lovidovi.models.UsersModel;
import com.example.lovidovi.networking.RetrofitClient;
import com.example.lovidovi.receivers.AppSignatureHashHelper;
import com.example.lovidovi.receivers.ZikySMSReceiver;
import com.example.lovidovi.utils.Constants;
import com.example.lovidovi.utils.SharedPreferencesConfig;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeVerificationActivity extends AppCompatActivity implements
        ZikySMSReceiver.OTPReceiveListener {
    EditText enter_code;
    Button confirm;
    RelativeLayout progress;
    TextView resend;
    ImageView resendCodeImage;
    String name,username,phone,code,password,confirmPassword,newpass,
            clientsName,clientsUsername,clientsPhone,token;
    private ZikySMSReceiver smsReceiver;
    private Boolean reset = false;
    public String appSignature;
    private Context context;
    private SharedPreferencesConfig sharedPreferencesConfig;
    private static final String RESET ="com.example.lovidovi.settings" ;
    private static final String RESETT ="com.example.lovidovi.auth" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);
        enter_code = findViewById(R.id.enter_signup_code);
        confirm = findViewById(R.id.confirm_signup_code);
        progress = findViewById(R.id.progressLoad);
        resend = findViewById(R.id.resendCode);
        resendCodeImage = findViewById(R.id.resendCodeImageView);
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());
        newpass = getIntent().getExtras().getString("NEWPASS");
        phone = getIntent().getExtras().getString("NUMBER");
        name = getIntent().getExtras().getString("NAME");
        username = getIntent().getExtras().getString("USER");
        password = getIntent().getExtras().getString("PASS");
        confirmPassword = getIntent().getExtras().getString("CONFIRM");
        if(getIntent().hasExtra(RESET)) {
            reset = getIntent().getBooleanExtra(RESET, false);
        }
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCode();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verification();
            }
        });
        context = getApplicationContext();
        appSignature = new AppSignatureHashHelper(this).getAppSignatures().get(0);
        sendCode();
    }
    private void sendCode() {
        showProgress();
        Call<SendCodeModel> call = RetrofitClient.getInstance(CodeVerificationActivity.this)
                .getApiConnector()
                .code(phone,appSignature);
        call.enqueue(new Callback<SendCodeModel>() {
            @Override
            public void onResponse(Call<SendCodeModel> call, Response<SendCodeModel> response) {
                hideProgress();
                if(response.code()==201){
                    startSMSListener();
                    startCountDownTimer();
                }
                else{
                    Toast.makeText(CodeVerificationActivity.this,"Server error",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SendCodeModel> call, Throwable t) {
                hideProgress();
                Toast.makeText(CodeVerificationActivity.this,t.getMessage()+"Network error. Check your connection",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void registerUserAfterConfirmation() {
        showProgress();
        code = enter_code.getText().toString();
        Call<UsersModel> call = RetrofitClient.getInstance(CodeVerificationActivity.this)
                .getApiConnector()
                .signUp(name,username,phone,password,confirmPassword,code);
        call.enqueue(new Callback<UsersModel>() {
            @Override
            public void onResponse(Call<UsersModel> call, Response<UsersModel> response) {
                hideProgress();
                if(response.code()==201){
                    token = response.body().getAccessToken();
                    clientsName = Integer.toString(response.body().getUser().getId());
                    clientsUsername = response.body().getUser().getUsername();
                    clientsPhone = response.body().getUser().getPhone();
                  //  clientsId = Integer.toString(response.body().getUser().getId());
                    sharedPreferencesConfig.saveAuthenticationInformation(token,clientsName,clientsUsername,clientsPhone, Constants.ACTIVE_CONSTANT);
                    Toast.makeText(CodeVerificationActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CodeVerificationActivity.this, MainActivity.class);
                    intent.putExtra(RESETT, true);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(CodeVerificationActivity.this,"response:"+response.message(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UsersModel> call, Throwable t) {
                hideProgress();
                Toast.makeText(CodeVerificationActivity.this,"errot:"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void startSMSListener() {
        try {
            smsReceiver = new ZikySMSReceiver();
            smsReceiver.setOTPListener(CodeVerificationActivity.this);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            this.registerReceiver(smsReceiver, intentFilter);

            SmsRetrieverClient client = SmsRetriever.getClient(this);

            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // API successfully started
                    Log.d("OTP-API:", "Successfully Started");
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Fail to start API
                    //Reload Activity or what?

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void startCountDownTimer(){

        new CountDownTimer(60000, 1000) { // 60 seconds, in 1 second intervals
            public void onTick(long millisUntilFinished) {
                resend.setText("Resend code after "+millisUntilFinished / 1000 +" Seconds");
                resend.setVisibility(View.VISIBLE);
                resendCodeImage.setVisibility(View.GONE);
                resend.setEnabled(false);
            }

            public void onFinish() {
                resend.setEnabled(true);
                resendCodeImage.setVisibility(View.VISIBLE);
                resend.setText("Resend Code");

            }
        }.start();
    }
    @Override
    public void onOTPReceived(String verificationSMS) {
        //success
//        Log.d("OTP-SUCCESS:", verificationSMS);
//        Toast.makeText(this, "OTP Success", Toast.LENGTH_SHORT).show();

        //EXTRACT THE 5-digit Code
        enter_code.setText(extractCodeFromSMS(verificationSMS));

        //Start Verification too :)
        verification();
        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver);
        }
    }

    private void verification() {
        if (!reset){
            registerUserAfterConfirmation();
        }else{
            changePassword();
        }
    }

    @Override
    public void onOTPTimeOut() {
        Log.d("OTP-TIMEOUT:", "TIMEOUT");
        Toast.makeText(this, "TIME-OUT, Try Again", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onOTPReceivedError(String error) {
        //error
        Log.d("OTP-ERROR:", error);
        Toast.makeText(this, "Error: "+error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver);
        }
    }

    public String extractCodeFromSMS(String verificationSMS) {
        return verificationSMS.split(":")[1];
    }
    public void resendSMS() {
        enter_code.setText(""); // Clear Code
        sendCode();
    }
    private void changePassword() {
        showProgress();
        String code = enter_code.getText().toString();
        Call<ChangedForgotPassModel> call = RetrofitClient.getInstance(this)
                .getApiConnector()
                .newPass(code,newpass,phone);
        call.enqueue(new Callback<ChangedForgotPassModel>() {
            @Override
            public void onResponse(Call<ChangedForgotPassModel> call, Response<ChangedForgotPassModel> response) {
                hideProgress();
                if(response.isSuccessful()){
                    Toast.makeText(CodeVerificationActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    token = response.body().getAccessToken();
                    clientsName = Integer.toString(response.body().getUser().getId());
                    clientsUsername = response.body().getUser().getUsername();
                    clientsPhone = response.body().getUser().getPhone();
                    sharedPreferencesConfig.saveAuthenticationInformation(token,clientsName,clientsUsername,clientsPhone, Constants.ACTIVE_CONSTANT);
                    Intent intent = new Intent(CodeVerificationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(CodeVerificationActivity.this,"response:"+response.message(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ChangedForgotPassModel> call, Throwable t) {
                hideProgress();
                Toast.makeText(CodeVerificationActivity.this,"errot:"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showProgress(){
        progress.setVisibility(View.VISIBLE);
    }
    private void hideProgress(){
        progress.setVisibility(View.GONE);
    }
}
