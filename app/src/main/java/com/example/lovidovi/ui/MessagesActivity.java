package com.example.lovidovi.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lovidovi.R;
import com.example.lovidovi.adapters.MessagesAdapter;
import com.example.lovidovi.models.MessagesModel;
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
    String chat_id,title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        recyclerView = findViewById(R.id.messagesRecyclerView);
        messagesAdapter = new MessagesAdapter(MessagesActivity.this,mMessagesArrayList);
        recyclerView.setAdapter(messagesAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(MessagesActivity.this,getResources().getInteger(R.integer.product_grid_span)));
        chat_id = getIntent().getExtras().getString("CHATID");
        title = getIntent().getExtras().getString("USERNAME");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(title);
        viewMessages();
    }
    private void viewMessages() {
        Call<List<MessagesModel>> call = RetrofitClient.getInstance(MessagesActivity.this)
                .getApiConnector()
                .getM(chat_id);
        call.enqueue(new Callback<List<MessagesModel>>() {
            @Override
            public void onResponse(Call<List<MessagesModel>> call, Response<List<MessagesModel>> response) {

                // hideProgress();
                if(response.code()==200){
                    mMessagesArrayList.addAll(response.body());
                    messagesAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(MessagesActivity.this,"Internal server error. Please retry",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<MessagesModel>> call, Throwable t) {

                // hideProgress();
                Toast.makeText(MessagesActivity.this,"Network error",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
