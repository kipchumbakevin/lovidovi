package com.example.lovidovi.ui;


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
import com.example.lovidovi.adapters.QuotesAdapter;
import com.example.lovidovi.models.QuotesModel;
import com.example.lovidovi.networking.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    private ArrayList<QuotesModel>mQuotesArrayList = new ArrayList<>();
    QuotesAdapter quotesAdapter;
    FloatingActionButton floatingActionButton;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.quaotesrecyclerview);
        quotesAdapter = new QuotesAdapter(getActivity(),mQuotesArrayList);
        recyclerView.setAdapter(quotesAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),getResources().getInteger(R.integer.product_grid_span)));
        floatingActionButton = view.findViewById(R.id.fab);
        viewQuotes();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQ();
            }
        });

        return view;
    }

    private void addQ() {
        TextView cancel,done;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.addquote, null);
        final EditText enterquote = view.findViewById(R.id.crushphone);
        cancel = view.findViewById(R.id.cancel);
        done = view.findViewById(R.id.done);

        alertDialogBuilder.setView(view);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void viewQuotes() {
        Call<List<QuotesModel>> call = RetrofitClient.getInstance(getActivity())
                .getApiConnector()
                .getQuotes();
        call.enqueue(new Callback<List<QuotesModel>>() {
            @Override
            public void onResponse(Call<List<QuotesModel>> call, Response<List<QuotesModel>> response) {
                if (response.isSuccessful()) {
                    mQuotesArrayList.addAll(response.body());
                    quotesAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getActivity(),"Server error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<QuotesModel>> call, Throwable t) {
                Toast.makeText(getActivity(),"Network error",Toast.LENGTH_SHORT).show();
            }

        });
    }

}
