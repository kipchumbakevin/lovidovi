package com.kipchulovidovi.lovidovi.ui;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.kipchulovidovi.lovidovi.R;
import com.kipchulovidovi.lovidovi.adapters.ChatsAdapter;
import com.kipchulovidovi.lovidovi.models.ChatsModel;
import com.kipchulovidovi.lovidovi.models.SignUpMessagesModel;
import com.kipchulovidovi.lovidovi.networking.RetrofitClient;
import com.kipchulovidovi.lovidovi.utils.SharedPreferencesConfig;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
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
    private final int REQUEST_CODE=99;
    RelativeLayout progressLyt;
    CountryCodePicker ccp;
    EditText pp;
    ConstraintLayout no_messages;
    private InterstitialAd mInterstitialAd;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;


    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        recyclerView = view.findViewById(R.id.messagesrecycler);
        progressLyt = view.findViewById(R.id.progressLoad);
        no_messages = view.findViewById(R.id.nomessages);
        chatsAdapter = new ChatsAdapter(getActivity(),mMessagesArrayList);
        recyclerView.setAdapter(chatsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        sharedPreferencesConfig = new SharedPreferencesConfig(getActivity());
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-4220777685017021/2887406340");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
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
        ccp = view.findViewById(R.id.ccp);
        ImageView contacts = view.findViewById(R.id.gotocontacts);
         pp = view.findViewById(R.id.phoneP);
         progressLyt = view.findViewById(R.id.progressLoad);
        cancel = view.findViewById(R.id.cancel);
        send = view.findViewById(R.id.done);
        ccp.registerCarrierNumberEditText(pp);

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
                requestContactPermission();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
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
                }
                if (!ccp.isValidFullNumber()){
                    Toast.makeText(getActivity(),"Enter a valid number",Toast.LENGTH_SHORT).show();
                }
                else {
                    String phone = ccp.getFullNumberWithPlus();
                    String mess = message.getText().toString();
                    showProgress();
                    Call<SignUpMessagesModel> call = RetrofitClient.getInstance(getActivity())
                            .getApiConnector()
                            .sendM(phone, mess);
                    call.enqueue(new Callback<SignUpMessagesModel>() {
                        @Override
                        public void onResponse(Call<SignUpMessagesModel> call, Response<SignUpMessagesModel> response) {
                            hideProgress();
                            if (response.code() == 201) {
                                alertDialog.dismiss();
                                viewMessages();
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Server error", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<SignUpMessagesModel> call, Throwable t) {
                            hideProgress();
                            Toast.makeText(getActivity(), "Network error.Check your connection", Toast.LENGTH_LONG).show();
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

            private void viewMessages() {
        no_messages.setVisibility(View.GONE);
        showProgress();
        mMessagesArrayList.clear();
                Call<List<ChatsModel>> call = RetrofitClient.getInstance(getActivity())
                        .getApiConnector()
                        .getChats();
                call.enqueue(new Callback<List<ChatsModel>>() {
                    @Override
                    public void onResponse(Call<List<ChatsModel>> call, Response<List<ChatsModel>> response) {
                        hideProgress();
                        if (response.isSuccessful()) {
                            if (response.body().size()>0){
                                mMessagesArrayList.addAll(response.body());
                                chatsAdapter.notifyDataSetChanged();
                            }else {
                                no_messages.setVisibility(View.VISIBLE);
                            }

                        } else {
                            Toast.makeText(getActivity(), "Server error " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ChatsModel>> call, Throwable t) {
                        hideProgress();
                        Toast.makeText(getActivity(), "Network error. Check your connection", Toast.LENGTH_SHORT).show();
                    }

                });
            }
    private void hideProgress() {
        progressLyt.setVisibility(View.INVISIBLE);
    }

    private void showProgress() {
        progressLyt.setVisibility(View.VISIBLE);
    }
    public void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Read Contacts permission");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable access to contacts.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.READ_CONTACTS}
                                    , PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    Toast.makeText(getActivity(), "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
