package com.example.lovidovi.ui;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovidovi.R;
import com.example.lovidovi.adapters.NotificationsAdapter;
import com.example.lovidovi.models.ReceiveNotificationsModel;
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
public class NotificationsFragment extends Fragment {
    RecyclerView recyclerView;
    NotificationsAdapter notificationsAdapter;
    FloatingActionButton floatingActionButton;
    private ArrayList<ReceiveNotificationsModel> mNotificationsArrayList = new ArrayList<>();
    SharedPreferencesConfig sharedPreferencesConfig;
    private final int REQUEST_CODE=99;
    EditText enterNum;


    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerView = view.findViewById(R.id.notificationsrecycler);
        notificationsAdapter = new NotificationsAdapter(getActivity(),mNotificationsArrayList);
        recyclerView.setAdapter(notificationsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        sharedPreferencesConfig = new SharedPreferencesConfig(getActivity());
        floatingActionButton = view.findViewById(R.id.fab);
        viewNotifications();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial();
            }
        });
        readall();
        return view;

    }

    private void readall() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        Call<SignUpMessagesModel> call = RetrofitClient.getInstance(getActivity())
                .getApiConnector()
                .readAllN(phone);
        call.enqueue(new Callback<SignUpMessagesModel>() {
            @Override
            public void onResponse(Call<SignUpMessagesModel> call, Response<SignUpMessagesModel> response) {
                if (response.code() == 201) {
                } else {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SignUpMessagesModel> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage() + "error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void dial() {
        TextView cancel,done;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.addcrush, null);
        enterNum = view.findViewById(R.id.crushphone);
        ImageView contacts = view.findViewById(R.id.gotocontacts);
        cancel = view.findViewById(R.id.cancel);
        done = view.findViewById(R.id.done);

        alertDialogBuilder.setView(view);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enterNum.getText().toString().isEmpty()){
                    enterNum.setError("Required");
                }else {
                    String phone = enterNum.getText().toString();
                    Call<SignUpMessagesModel> call = RetrofitClient.getInstance(getActivity())
                            .getApiConnector()
                            .notifyCrush(phone);
                    call.enqueue(new Callback<SignUpMessagesModel>() {
                        @Override
                        public void onResponse(Call<SignUpMessagesModel> call, Response<SignUpMessagesModel> response) {
                            if (response.code() == 200) {
                                alertDialog.dismiss();
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
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

    }

    @Override
    public void onActivityResult(int reqCode, int resultCode,Intent data) {
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
                                enterNum.setText(num);
                            }
                        }
                    }
                    break;
                }
        }
    }

    private void viewNotifications() {
        final String phone = sharedPreferencesConfig.readClientsPhone();
        Call<List<ReceiveNotificationsModel>> call = RetrofitClient.getInstance(getActivity())
                .getApiConnector()
                .getNotifications(phone);
        call.enqueue(new Callback<List<ReceiveNotificationsModel>>() {
            @Override
            public void onResponse(Call<List<ReceiveNotificationsModel>> call, Response<List<ReceiveNotificationsModel>> response) {

               // hideProgress();
                if(response.code()==200){
                    mNotificationsArrayList.addAll(response.body());
                    notificationsAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getActivity(),"Internal server error. Please retry",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<ReceiveNotificationsModel>> call, Throwable t) {

               // hideProgress();
                Toast.makeText(getContext(),"Network error",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
