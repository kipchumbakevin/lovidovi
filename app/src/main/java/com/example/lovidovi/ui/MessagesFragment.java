package com.example.lovidovi.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lovidovi.R;
import com.example.lovidovi.adapters.MessagesAdapter;
import com.example.lovidovi.models.ChatsModel;
import com.example.lovidovi.models.MessagesModel;
import com.example.lovidovi.networking.RetrofitClient;
import com.example.lovidovi.utils.SharedPreferencesConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {
    RecyclerView recyclerView;
    private ArrayList<ChatsModel>mMessagesArrayList = new ArrayList<>();
    MessagesAdapter messagesAdapter;
    SharedPreferencesConfig sharedPreferencesConfig;


    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        recyclerView = view.findViewById(R.id.messagesrecycler);
        messagesAdapter = new MessagesAdapter(getActivity(),mMessagesArrayList);
        recyclerView.setAdapter(messagesAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),getResources().getInteger(R.integer.product_grid_span)));
        sharedPreferencesConfig = new SharedPreferencesConfig(getActivity());
        viewMessages();
        return view;
    }

    private void viewMessages() {
        String access_token = sharedPreferencesConfig.readClientsAccessToken();
        Call<List<ChatsModel>> call = RetrofitClient.getInstance(getActivity())
                .getApiConnector()
                .getChats(access_token);
        call.enqueue(new Callback<List<ChatsModel>>() {
            @Override
            public void onResponse(Call<List<ChatsModel>> call, Response<List<ChatsModel>> response) {
                if (response.isSuccessful()) {
                    mMessagesArrayList.addAll(response.body());
                    messagesAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getActivity(),"Server error "+response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatsModel>> call, Throwable t) {
                Log.d("lll", "failed "+t.getMessage());
                Toast.makeText(getActivity(),"Network error",Toast.LENGTH_SHORT).show();
            }

        });
    }

}
