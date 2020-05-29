package com.example.lovidovi.ui;


import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovidovi.R;
import com.example.lovidovi.adapters.ChatsAdapter;
import com.example.lovidovi.models.ChatsModel;
import com.example.lovidovi.models.SignUpMessagesModel;
import com.example.lovidovi.networking.RetrofitClient;
import com.example.lovidovi.utils.SharedPreferencesConfig;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    ChatsAdapter chatsAdapter;
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
        chatsAdapter = new ChatsAdapter(getActivity(),mMessagesArrayList);
        recyclerView.setAdapter(chatsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),getResources().getInteger(R.integer.product_grid_span)));
        sharedPreferencesConfig = new SharedPreferencesConfig(getActivity());
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMessage();
            }
        });
        viewMessages();
        return view;
    }

    private void newMessage() {
        TextView cancel, send;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.addmessage, null);
        final EditText message = view.findViewById(R.id.messageM);
        final EditText pp = view.findViewById(R.id.phoneP);
        cancel = view.findViewById(R.id.cancel);
        send = view.findViewById(R.id.done);

        alertDialogBuilder.setView(view);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.getText().toString().isEmpty()) {
                    message.setError("Required");
                }
                if (pp.getText().toString().isEmpty()) {
                    pp.setError("Required");
                } else {
                    String phone = pp.getText().toString();
                    String mess = message.getText().toString();
                    Call<SignUpMessagesModel> call = RetrofitClient.getInstance(getActivity())
                            .getApiConnector()
                            .sendM(phone, mess);
                    call.enqueue(new Callback<SignUpMessagesModel>() {
                        @Override
                        public void onResponse(Call<SignUpMessagesModel> call, Response<SignUpMessagesModel> response) {
                            if (response.isSuccessful()) {
                                alertDialog.dismiss();
                                Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Server error", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<SignUpMessagesModel> call, Throwable t) {
                            Toast.makeText(getActivity(), t.getMessage() + "error", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

            private void viewMessages() {
                Call<List<ChatsModel>> call = RetrofitClient.getInstance(getActivity())
                        .getApiConnector()
                        .getChats();
                call.enqueue(new Callback<List<ChatsModel>>() {
                    @Override
                    public void onResponse(Call<List<ChatsModel>> call, Response<List<ChatsModel>> response) {
                        if (response.isSuccessful()) {
                            mMessagesArrayList.addAll(response.body());
                            chatsAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "Server error " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ChatsModel>> call, Throwable t) {
                        Log.d("lll", "failed " + t.getMessage());
                        Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
                    }

                });
            }
}
