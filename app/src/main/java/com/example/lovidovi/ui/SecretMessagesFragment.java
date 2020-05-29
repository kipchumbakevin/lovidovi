package com.example.lovidovi.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lovidovi.R;
import com.example.lovidovi.adapters.ChatsAdapter;
import com.example.lovidovi.models.ChatsModel;
import com.example.lovidovi.utils.SharedPreferencesConfig;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecretMessagesFragment extends Fragment {
    RecyclerView recyclerView;
    ChatsAdapter chatsAdapter;
    private ArrayList<ChatsModel>mMessagesArrayList = new ArrayList<>();
    SharedPreferencesConfig sharedPreferencesConfig;


    public SecretMessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_secret_messages, container, false);
//        recyclerView = view.findViewById(R.id.secretrecycler);
//        chatsAdapter = new ChatsAdapter(getActivity(),mMessagesArrayList);
//        recyclerView.setAdapter(chatsAdapter);
//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),getResources().getInteger(R.integer.product_grid_span)));
//        sharedPreferencesConfig = new SharedPreferencesConfig(getActivity());
//        viewmS();
        return view;
    }

//    private void viewmS() {
//        final String phone = sharedPreferencesConfig.readClientsPhone();
//        Call<List<MessagesModel>> call = RetrofitClient.getInstance(getActivity())
//                .getApiConnector()
//                .getM(phone);
//        call.enqueue(new Callback<List<MessagesModel>>() {
//            @Override
//            public void onResponse(Call<List<MessagesModel>> call, Response<List<MessagesModel>> response) {
//
//                // hideProgress();
//                if(response.code()==200){
//                    mMessagesArrayList.addAll(response.body());
//                    chatsAdapter.notifyDataSetChanged();
//                }
//                else{
//                    Toast.makeText(getActivity(),"Internal server error. Please retry",Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<List<MessagesModel>> call, Throwable t) {
//
//                // hideProgress();
//                Toast.makeText(getContext(),"Network error",Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


}
