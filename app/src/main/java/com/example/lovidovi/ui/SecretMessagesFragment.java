package com.example.lovidovi.ui;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovidovi.R;
import com.example.lovidovi.adapters.ChatsAdapter;
import com.example.lovidovi.adapters.SecretChatsAdapter;
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
public class SecretMessagesFragment extends Fragment {
    RecyclerView recyclerView;
    SecretChatsAdapter secretChatsAdapter;
    private ArrayList<ChatsModel>mSecretArrayList = new ArrayList<>();
    SharedPreferencesConfig sharedPreferencesConfig;
    EditText pp;
    private final int REQUEST_CODE=99;
    public SecretMessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_secret_messages, container, false);
        recyclerView = view.findViewById(R.id.secretrecycler);
        secretChatsAdapter = new SecretChatsAdapter(getActivity(),mSecretArrayList);
        recyclerView.setAdapter(secretChatsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        sharedPreferencesConfig = new SharedPreferencesConfig(getActivity());
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSecret();
            }
        });
        viewmS();
        return view;
    }

    private void sendSecret() {
        TextView cancel, send;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.addsecretmessage, null);
        final EditText message = view.findViewById(R.id.messageM);
        ImageView contacts = view.findViewById(R.id.gotocontacts);
        pp = view.findViewById(R.id.phoneP);
        cancel = view.findViewById(R.id.cancel);
        send = view.findViewById(R.id.done);

        alertDialogBuilder.setView(view);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        message.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                message.setTextIsSelectable(true);
                return false;
            }
        });
        pp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                pp.setTextIsSelectable(true);
                return false;
            }
        });
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
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
                            .sendsecretM(phone, mess);
                    call.enqueue(new Callback<SignUpMessagesModel>() {
                        @Override
                        public void onResponse(Call<SignUpMessagesModel> call, Response<SignUpMessagesModel> response) {
                            if (response.code() == 201) {
                                Intent intent = new Intent(getActivity(),MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                                alertDialog.dismiss();
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
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
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (REQUEST_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getActivity().getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                pp.setText(num);
                            }
                        }
                    }
                    break;
                }
        }

    }

    private void viewmS() {
        Call<List<ChatsModel>> call = RetrofitClient.getInstance(getActivity())
                .getApiConnector()
                .getsecretChats();
        call.enqueue(new Callback<List<ChatsModel>>() {
            @Override
            public void onResponse(Call<List<ChatsModel>> call, Response<List<ChatsModel>> response) {
                if (response.isSuccessful()) {
                    mSecretArrayList.addAll(response.body());
                    secretChatsAdapter.notifyDataSetChanged();
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
