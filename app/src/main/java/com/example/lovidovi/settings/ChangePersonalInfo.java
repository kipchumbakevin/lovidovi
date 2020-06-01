package com.example.lovidovi.settings;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovidovi.R;
import com.example.lovidovi.models.ChangeDetailsModel;
import com.example.lovidovi.models.SignUpMessagesModel;
import com.example.lovidovi.networking.RetrofitClient;
import com.example.lovidovi.utils.SharedPreferencesConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePersonalInfo extends AppCompatActivity {
    RelativeLayout progress,username;
    TextView editUsername;
    SharedPreferencesConfig sharedPreferencesConfig;
    private String clientsName,clientsUsername;
    int dd = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_personal_info);
        editUsername = findViewById(R.id.set_username);
        username = findViewById(R.id.username_relative);
        progress = findViewById(R.id.progressLoad);
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        loadDetails();

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dd = 1;
                changeDetailsDialog();
            }
        });

    }
    private void changeDetailsDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.change_details_edittext,null);
        TextView title = view.findViewById(R.id.dialog_title);
        final EditText editText = view.findViewById(R.id.edit_them_details);
        if (dd==1) title.setText("Username:");
        alertDialogBuilder
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String changed = editText.getText().toString();
                        if (!editText.getText().toString().isEmpty()){
                            showProgress();
                            Call<ChangeDetailsModel> call = RetrofitClient.getInstance(ChangePersonalInfo.this)
                                    .getApiConnector()
                                    .changeDetails(changed);
                            call.enqueue(new Callback<ChangeDetailsModel>() {
                                @Override
                                public void onResponse(Call<ChangeDetailsModel> call, Response<ChangeDetailsModel> response) {
                                    hideProgress();
                                    if(response.isSuccessful()){
                                        editUsername.setText(changed);
                                        clientsName = response.body().getUser().getName();
                                        clientsUsername = response.body().getUser().getUsername();
                                        sharedPreferencesConfig.saveChangedDetails(clientsName,clientsUsername);
                                        Toast.makeText(ChangePersonalInfo.this,"Done",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ChangePersonalInfo.this, SettingsActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(ChangePersonalInfo.this,"The username has already been taken",Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<ChangeDetailsModel> call, Throwable t) {
                                    hideProgress();
                                    Toast.makeText(ChangePersonalInfo.this,"errot:"+t.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
        alertDialogBuilder.setView(view);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void loadDetails() {
        editUsername.setText(sharedPreferencesConfig.readClientsUsername());
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }
    private void hideProgress(){
        progress.setVisibility(View.GONE);
    }

}
