package com.kipchulovidovi.lovidovi.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kipchulovidovi.lovidovi.R;
import com.kipchulovidovi.lovidovi.adapters.MessagesAdapter;
import com.kipchulovidovi.lovidovi.models.MessagesModel;
import com.kipchulovidovi.lovidovi.models.SignUpMessagesModel;
import com.kipchulovidovi.lovidovi.networking.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesActivity extends AppCompatActivity {
    private ArrayList<MessagesModel>mMessagesArrayList = new ArrayList<>();
    MessagesAdapter messagesAdapter;
    RecyclerView recyclerView;
    String chat_id,title,phone,simu,r,secret;
    private static final String KNOW = "com.kipchulovidovi.lovidovi.adapters";
    ImageView send;
    EditText typeamessage;
    TextView secree,sec;
    private final int REQUEST_CODE=99;
    RelativeLayout progressLyt;
    private static final String MESS ="com.kipchulovidovi.lovidovi.ui";
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
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
        secree = findViewById(R.id.secree);
        sec = findViewById(R.id.sec);
        send = findViewById(R.id.sendthemsg);
        chat_id = getIntent().getExtras().getString("CHATID");
        title = getIntent().getExtras().getString("USERNAME");
        phone = getIntent().getExtras().getString("PHONE");
        simu = getIntent().getExtras().getString("SIMU");
        r = getIntent().getExtras().getString("DEL");
        secret = getIntent().getExtras().getString("SECRET");
        if(getIntent().hasExtra(KNOW)) {
            reset = getIntent().getBooleanExtra(KNOW, false);
        }
        if (r.equals(Integer.toString(1))){
            Toast.makeText(MessagesActivity.this,"Messages in this chat have been deleted.",Toast.LENGTH_SHORT).show();
        }
        secree.setText(chat_id);
        sec.setText(secret);
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
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!typeamessage.getText().toString().isEmpty()){
                    if (reset){
                        sendDeMessage();
                    }else{
                        sendDeSecretMessage();
                    }
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
                    View view = MessagesActivity.this.getCurrentFocus();
                    if (view != null){
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                    }
                    viewSecretAfterSend();
                    Toast.makeText(MessagesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MessagesActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<SignUpMessagesModel> call, Throwable t) {
                hideProgress();
                Toast.makeText(MessagesActivity.this, "Network error. Check your connection.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MessagesActivity.this, "Network error. Check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void viewSecretAfterSend() {
        mMessagesArrayList.clear();
        chat_id = secree.getText().toString();
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
                Toast.makeText(MessagesActivity.this, "Network error. Check your connection.", Toast.LENGTH_SHORT).show();
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
                    View view = MessagesActivity.this.getCurrentFocus();
                    if (view != null){
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                    }
                    viewMessagesAfterSend();
                    Toast.makeText(MessagesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MessagesActivity.this, "Server error", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SignUpMessagesModel> call, Throwable t) {
                hideProgress();
                Toast.makeText(MessagesActivity.this, "Network error. Check your connection.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MessagesActivity.this, "Network error. Check your connection.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void viewMessagesAfterSend() {
        mMessagesArrayList.clear();
        chat_id = secree.getText().toString();
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
                Toast.makeText(MessagesActivity.this, "Network error. Check your connection.", Toast.LENGTH_SHORT).show();
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
