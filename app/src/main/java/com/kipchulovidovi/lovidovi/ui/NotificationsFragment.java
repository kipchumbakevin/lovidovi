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

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.hbb20.CountryCodePicker;
import com.kipchulovidovi.lovidovi.R;
import com.kipchulovidovi.lovidovi.adapters.NotificationsAdapter;
import com.kipchulovidovi.lovidovi.models.ReceiveNotificationsModel;
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
public class NotificationsFragment extends Fragment {
    RecyclerView recyclerView;
    NotificationsAdapter notificationsAdapter;
    FloatingActionButton floatingActionButton;
    private ArrayList<ReceiveNotificationsModel> mNotificationsArrayList = new ArrayList<>();
    SharedPreferencesConfig sharedPreferencesConfig;
    private final int REQUEST_CODE=99;
    EditText enterNum;
    RelativeLayout progressLyt;
    CountryCodePicker ccp;
    ConstraintLayout no;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;

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
        progressLyt = view.findViewById(R.id.progressLoad);
        no = view.findViewById(R.id.nonotifications);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        sharedPreferencesConfig = new SharedPreferencesConfig(getActivity());
        floatingActionButton = view.findViewById(R.id.fab);
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-4220777685017021/2887406340");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
                   // Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SignUpMessagesModel> call, Throwable t) {
               // Toast.makeText(getActivity(), t.getMessage() + "error", Toast.LENGTH_LONG).show();
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
        progressLyt = view.findViewById(R.id.progressLoad);
        ccp = view.findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(enterNum);

        alertDialogBuilder.setView(view);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enterNum.getText().toString().isEmpty()){
                    enterNum.setError("Required");
                }
                if (!ccp.isValidFullNumber()){
                    Toast.makeText(getActivity(),"Enter a valid number",Toast.LENGTH_SHORT).show();
                }
                else {
                    String phone = ccp.getFullNumberWithPlus();
                    showProgress();
                    Call<SignUpMessagesModel> call = RetrofitClient.getInstance(getActivity())
                            .getApiConnector()
                            .notifyCrush(phone);
                    call.enqueue(new Callback<SignUpMessagesModel>() {
                        @Override
                        public void onResponse(Call<SignUpMessagesModel> call, Response<SignUpMessagesModel> response) {
                            hideProgress();
                            if (response.code() == 201) {
                                alertDialog.dismiss();
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                                if (mInterstitialAd.isLoaded()) {
                                    mInterstitialAd.show();
                                } else {
                                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                                }
                            } else {
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<SignUpMessagesModel> call, Throwable t) {
                            hideProgress();
                            Toast.makeText(getActivity(),  "Network error. Check your connection", Toast.LENGTH_LONG).show();
                        }
                    });
                }
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
        mNotificationsArrayList.clear();
        showProgress();
        final String phone = sharedPreferencesConfig.readClientsPhone();
        Call<List<ReceiveNotificationsModel>> call = RetrofitClient.getInstance(getActivity())
                .getApiConnector()
                .getNotifications(phone);
        call.enqueue(new Callback<List<ReceiveNotificationsModel>>() {
            @Override
            public void onResponse(Call<List<ReceiveNotificationsModel>> call, Response<List<ReceiveNotificationsModel>> response) {

                hideProgress();
                if(response.code()==200){
                    if (response.body().size()>0){
                        mNotificationsArrayList.addAll(response.body());
                        notificationsAdapter.notifyDataSetChanged();
                    }else {
                        no.setVisibility(View.VISIBLE);
                    }

                }
                else{
                    Toast.makeText(getActivity(),"Internal server error. Please retry",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<ReceiveNotificationsModel>> call, Throwable t) {

                hideProgress();
                Toast.makeText(getContext(),"Network error",Toast.LENGTH_SHORT).show();
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
