package com.example.lovidovi.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lovidovi.R;
import com.example.lovidovi.adapters.MessagesAdapter;
import com.example.lovidovi.models.MessagesModel;
import com.example.lovidovi.models.SignUpMessagesModel;
import com.example.lovidovi.networking.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesActivity extends AppCompatActivity {
    private ArrayList<MessagesModel>mMessagesArrayList = new ArrayList<>();
    MessagesAdapter messagesAdapter;
    RecyclerView recyclerView;
    String chat_id,title,phone,simu;
    private static final String KNOW = "com.example.lovidovi.adapters";
    ImageView send;
    EditText typeamessage;
    RelativeLayout progressLyt;
    private static final String MESS ="com.example.lovidovi.ui";
    private Boolean reset = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        recyclerView = findViewById(R.id.messagesRecyclerView);
        messagesAdapter = new MessagesAdapter(MessagesActivity.this,mMessagesArrayList);
        recyclerView.setAdapter(messagesAdapter);
        progressLyt = findViewById(R.id.progressLoad);
        typeamessage = findViewById(R.id.typeamessage);
        send = findViewById(R.id.sendthemsg);
        chat_id = getIntent().getExtras().getString("CHATID");
        title = getIntent().getExtras().getString("USERNAME");
        phone = getIntent().getExtras().getString("PHONE");
        simu = getIntent().getExtras().getString("SIMU");
        if(getIntent().hasExtra(KNOW)) {
            reset = getIntent().getBooleanExtra(KNOW, false);
        }
        ActionBar actionBar = getSupportActionBar();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        typeamessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                typeamessage.setTextIsSelectable(true);
                return false;
            }
        });
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(title);
        if (reset){
            viewMessages();
        }else{
            viewSecret();
        }
        typeamessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                send.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()!=0){
                    send.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reset){
                    sendDeMessage();
                }else{
                    sendDeSecretMessage();
                }
            }
        });
    }

    private void sendDeSecretMessage() {
        String mess = typeamessage.getText().toString();
        showProgress();
        Call<SignUpMessagesModel> call = RetrofitClient.getInstance(MessagesActivity.this)
                .getApiConnector()
                .sendsecretM(simu, mess);
        call.enqueue(new Callback<SignUpMessagesModel>() {
            @Override
            public void onResponse(Call<SignUpMessagesModel> call, Response<SignUpMessagesModel> response) {
                hideProgress();
                if (response.code()==201) {
                    typeamessage.getText().clear();
                    Intent intent = new Intent(MessagesActivity.this,MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(MessagesActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MessagesActivity.this, "Server error", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SignUpMessagesModel> call, Throwable t) {
                hideProgress();
                Toast.makeText(MessagesActivity.this, t.getMessage() + "error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void viewSecret() {
        mMessagesArrayList.clear();
        showProgress();
        Call<List<MessagesModel>> call = RetrofitClient.getInstance(MessagesActivity.this)
                .getApiConnector()
                .getsecretM(chat_id);
        call.enqueue(new Callback<List<MessagesModel>>() {
            @Override
            public void onResponse(Call<List<MessagesModel>> call, Response<List<MessagesModel>> response) {

                 hideProgress();
                if(response.code()==200){
                    reeead();
                    mMessagesArrayList.addAll(response.body());
                    messagesAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(MessagesActivity.this,"Internal server error. Please retry",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<MessagesModel>> call, Throwable t) {

                 hideProgress();
                Toast.makeText(MessagesActivity.this,"Network error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reeead() {
        Call<SignUpMessagesModel> call = RetrofitClient.getInstance(MessagesActivity.this)
                .getApiConnector()
                .readSS(chat_id);
        call.enqueue(new Callback<SignUpMessagesModel>() {
            @Override
            public void onResponse(Call<SignUpMessagesModel> call, Response<SignUpMessagesModel> response) {
                if (response.code() == 201) {

                } else {
                    // Toast.makeText(getActivity(), "Server error", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SignUpMessagesModel> call, Throwable t) {

                //Toast.makeText(getActivity(), t.getMessage() + "error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendDeMessage() {
        String mess = typeamessage.getText().toString();
        showProgress();
        Call<SignUpMessagesModel> call = RetrofitClient.getInstance(MessagesActivity.this)
                .getApiConnector()
                .sendM(phone, mess);
        call.enqueue(new Callback<SignUpMessagesModel>() {
            @Override
            public void onResponse(Call<SignUpMessagesModel> call, Response<SignUpMessagesModel> response) {
                hideProgress();
                if (response.code()==201) {
                    typeamessage.getText().clear();
                    Intent intent = new Intent(MessagesActivity.this,MainActivity.class);
                    intent.putExtra(MESS,true);
                    startActivity(intent);
                    finish();
                    Toast.makeText(MessagesActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MessagesActivity.this, "Server error", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SignUpMessagesModel> call, Throwable t) {
                hideProgress();
                Toast.makeText(MessagesActivity.this, t.getMessage() + "error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void viewMessages() {
        mMessagesArrayList.clear();
        showProgress();
        Call<List<MessagesModel>> call = RetrofitClient.getInstance(MessagesActivity.this)
                .getApiConnector()
                .getM(chat_id);
        call.enqueue(new Callback<List<MessagesModel>>() {
            @Override
            public void onResponse(Call<List<MessagesModel>> call, Response<List<MessagesModel>> response) {

                 hideProgress();
                if(response.code()==200){
                    read();
                    mMessagesArrayList.addAll(response.body());
                    messagesAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(MessagesActivity.this,"Internal server error. Please retry",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<MessagesModel>> call, Throwable t) {

                 hideProgress();
                Toast.makeText(MessagesActivity.this,"Network error",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void read() {
        Call<SignUpMessagesModel> call = RetrofitClient.getInstance(MessagesActivity.this)
                .getApiConnector()
                .readMM(chat_id);
        call.enqueue(new Callback<SignUpMessagesModel>() {
            @Override
            public void onResponse(Call<SignUpMessagesModel> call, Response<SignUpMessagesModel> response) {
                if (response.code() == 201) {

                } else {
                    // Toast.makeText(getActivity(), "Server error", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SignUpMessagesModel> call, Throwable t) {

                //Toast.makeText(getActivity(), t.getMessage() + "error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void hideProgress() {
        progressLyt.setVisibility(View.INVISIBLE);
    }

    private void showProgress() {
        progressLyt.setVisibility(View.VISIBLE);
    }
}
