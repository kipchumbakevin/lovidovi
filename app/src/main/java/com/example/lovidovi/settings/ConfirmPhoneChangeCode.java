package com.example.lovidovi.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lovidovi.R;
import com.example.lovidovi.models.ConfirmPhoneChangeCodeModel;
import com.example.lovidovi.models.GenerateCodeModel;
import com.example.lovidovi.networking.RetrofitClient;
import com.example.lovidovi.receivers.AppSignatureHashHelper;
import com.example.lovidovi.receivers.ZikySMSReceiver;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmPhoneChangeCode extends AppCompatActivity implements
        ZikySMSReceiver.OTPReceiveListener{
    EditText enterCode;
    Button confirmCode;
    String newphone,oldphone,pas;
    public String appSignature;
    private ZikySMSReceiver smsReceiver;
    RelativeLayout progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_phone_change_code);
        enterCode = findViewById(R.id.enter_pass_code);
        confirmCode = findViewById(R.id.submit_code);
        progress = findViewById(R.id.progressLoad);
        newphone = getIntent().getExtras().getString("NEWPHONE");
        oldphone = getIntent().getExtras().getString("OLDPHONE");
        pas = getIntent().getExtras().getString("PASSWORD");
        appSignature = new AppSignatureHashHelper(this).getAppSignatures().get(0);
        generate();
        confirmCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
    }

    private void generate() {
        showProgress();
        Call<GenerateCodeModel> call = RetrofitClient.getInstance(this)
                .getApiConnector()
                .generateCode(oldphone,appSignature);
        call.enqueue(new Callback<GenerateCodeModel>() {
            @Override
            public void onResponse(Call<GenerateCodeModel> call, Response<GenerateCodeModel> response) {
                hideProgress();
                if(response.code()==201){
                    startSMSListener();
                    startCountDownTimer();
                }
                else{
                    Toast.makeText(ConfirmPhoneChangeCode.this,"response:"+response.message(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GenerateCodeModel> call, Throwable t) {
                hideProgress();
                Toast.makeText(ConfirmPhoneChangeCode.this,"Network error",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void confirm() {
        showProgress();
        final String code = enterCode.getText().toString();
        Call<ConfirmPhoneChangeCodeModel> call = RetrofitClient.getInstance(ConfirmPhoneChangeCode.this)
                .getApiConnector()
                .changePhone(newphone,oldphone,code);
        call.enqueue(new Callback<ConfirmPhoneChangeCodeModel>() {
            @Override
            public void onResponse(Call<ConfirmPhoneChangeCodeModel> call, Response<ConfirmPhoneChangeCodeModel> response) {
                hideProgress();
                if(response.code()==201){
                    Toast.makeText(ConfirmPhoneChangeCode.this,"Your number has been changed successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConfirmPhoneChangeCode.this,SecurityOptions.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(ConfirmPhoneChangeCode.this,"response:"+response.message(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ConfirmPhoneChangeCodeModel> call, Throwable t) {
                hideProgress();
                Toast.makeText(ConfirmPhoneChangeCode.this,"Network error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startSMSListener() {
        try {
            smsReceiver = new ZikySMSReceiver();
            smsReceiver.setOTPListener(ConfirmPhoneChangeCode.this);

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
                //   textViewResendSMS.setText("Resend SMS after "+millisUntilFinished / 1000 +" Seconds");
                //   textViewResendSMS.setVisibility(View.VISIBLE);
            }

            public void onFinish() {
                //   textViewResendSMS.setText("Resend SMS");
                //   resendSMS.setV

            }
        }.start();
    }
    @Override
    public void onOTPReceived(String verificationSMS) {
        //success
//        Log.d("OTP-SUCCESS:", verificationSMS);
//        Toast.makeText(this, "OTP Success", Toast.LENGTH_SHORT).show();

        //EXTRACT THE 5-digit Code
        enterCode.setText(extractCodeFromSMS(verificationSMS));

        //Start Verification too :)
        confirm();
        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver);
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
        // shortCodeEditText.setText(""); // Clear Code
        generate();
    }
    private void showProgress(){
        progress.setVisibility(View.VISIBLE);
    }
    private void hideProgress(){
        progress.setVisibility(View.GONE);
    }
}
