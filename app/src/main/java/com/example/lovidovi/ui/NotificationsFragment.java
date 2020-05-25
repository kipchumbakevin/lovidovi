package com.example.lovidovi.ui;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),getResources().getInteger(R.integer.product_grid_span)));
        sharedPreferencesConfig = new SharedPreferencesConfig(getActivity());
        floatingActionButton = view.findViewById(R.id.fab);
        viewNotifications();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial();
            }
        });


        return view;
    }

    private void dial() {
        TextView cancel,done;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.addcrush, null);
        final EditText enterNum = view.findViewById(R.id.crushphone);
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
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

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
