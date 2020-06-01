package com.example.lovidovi.settings;

import androidx.appcompat.app.ActionBar;
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
import com.example.lovidovi.models.SignUpMessagesModel;
import com.example.lovidovi.networking.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePhoneNumber extends AppCompatActivity {
    EditText oldPhone,newPhone,enterPass;
    Button submit;
    String newnumber,oldphone,pas;
    RelativeLayout progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone_number);
//        oldPhone = findViewById(R.id.enter_old_phone);
//        newPhone = findViewById(R.id.enter_new_phone);
//        submit = findViewById(R.id.submit);
//        enterPass = findViewById(R.id.enter_password);
//        progress = findViewById(R.id.progressLoad);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar!=null){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isErrors()){
//                    Toast.makeText(ChangePhoneNumber.this,"Ensure you fill all fields",Toast.LENGTH_SHORT).show();
//                }if (!isNetworkAvailable()){
//                    Toast.makeText(ChangePhoneNumber.this,"Check your network connection",Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    generate();
//                }
//            }
//        });
//    }
//
//    private void generate() {
//        showProgress();
//        oldphone = oldPhone.getText().toString();
//        pas = enterPass.getText().toString();
//        newnumber = newPhone.getText().toString();
//        Call<SignUpMessagesModel> call = RetrofitClient.getInstance(this)
//                .getApiConnector()
//                .checkIfNoCorrect(oldphone,pas,newnumber);
//        call.enqueue(new Callback<SignUpMessagesModel>() {
//            @Override
//            public void onResponse(Call<SignUpMessagesModel> call, Response<SignUpMessagesModel> response) {
//                hideProgress();
//                if(response.code()==201){
//                    Intent intent = new Intent(ChangePhoneNumber.this,ConfirmPhoneChangeCode.class);
//                    intent.putExtra("OLDPHONE",oldphone);
//                    intent.putExtra("NEWPHONE",newnumber);
//                    startActivity(intent);
//                    finish();
//                    Toast.makeText(ChangePhoneNumber.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(ChangePhoneNumber.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SignUpMessagesModel> call, Throwable t) {
//                hideProgress();
//                Toast.makeText(ChangePhoneNumber.this,"errot:"+t.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//    private boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }
//    private boolean isErrors(){
//        if (oldPhone.getText().toString().isEmpty()){
//            return true;
//        }if (newPhone.getText().toString().isEmpty()){
//            return true;
//        }if (enterPass.getText().toString().isEmpty()){
//            return true;
//        }
//        return false;
//    }
//    private void showProgress() {
//        progress.setVisibility(View.VISIBLE);
//    }
//    private void hideProgress(){
//        progress.setVisibility(View.GONE);
//    }
    }
}
