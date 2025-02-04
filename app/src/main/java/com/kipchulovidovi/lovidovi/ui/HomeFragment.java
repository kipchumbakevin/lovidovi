package com.kipchulovidovi.lovidovi.ui;


import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kipchulovidovi.lovidovi.R;
import com.kipchulovidovi.lovidovi.adapters.MyQuotesAdapter;
import com.kipchulovidovi.lovidovi.adapters.QuotesAdapter;
import com.kipchulovidovi.lovidovi.models.QuotesModel;
import com.kipchulovidovi.lovidovi.models.SignUpMessagesModel;
import com.kipchulovidovi.lovidovi.networking.RetrofitClient;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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

    RecyclerView recyclerView,recycler2;
    RelativeLayout progressLyt;
    private ArrayList<QuotesModel>mQuotesArrayList = new ArrayList<>();
    private ArrayList<QuotesModel>mMyQuotesArrayList = new ArrayList<>();
    QuotesAdapter quotesAdapter;
    MyQuotesAdapter mm;
    FloatingActionButton floatingActionButton;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.quaotesrecyclerview);
        recycler2 = view.findViewById(R.id.myquotes);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycler2.setLayoutManager(linearLayoutManager);
        quotesAdapter = new QuotesAdapter(getActivity(),mQuotesArrayList);
        mm = new MyQuotesAdapter(getActivity(),mMyQuotesArrayList);
        recyclerView.setAdapter(quotesAdapter);
        recycler2.setAdapter(mm);
        progressLyt = view.findViewById(R.id.progressLoad);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),getResources().getInteger(R.integer.product_grid_span)));
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
        viewQuotes();
        viewMyQuotes();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQ();
            }
        });
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        return view;
    }

    private void addQ() {
        final TextView cancel,done;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.addquote, null);
        final EditText enterquote = view.findViewById(R.id.enterquote);
        cancel = view.findViewById(R.id.cancel);
        done = view.findViewById(R.id.done);
        progressLyt = view.findViewById(R.id.progressLoad);

        alertDialogBuilder.setView(view);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        enterquote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
               enterquote.setTextIsSelectable(true);
               return false;
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
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enterquote.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"You cant add an empty quote",Toast.LENGTH_SHORT).show();
                }if (enterquote.getText().length()<10){
                    Toast.makeText(getActivity(),"Too short",Toast.LENGTH_SHORT).show();
                }
                else {
                    String quote = enterquote.getText().toString();
                    showProgress();
                    Call<SignUpMessagesModel> call = RetrofitClient.getInstance(getActivity())
                            .getApiConnector()
                            .addQuote(quote);
                    call.enqueue(new Callback<SignUpMessagesModel>() {
                        @Override
                        public void onResponse(Call<SignUpMessagesModel> call, Response<SignUpMessagesModel> response) {
                            hideProgress();
                            if (response.code() == 201) {
                                viewQuotes();
                                viewMyQuotes();
                                alertDialog.dismiss();
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<SignUpMessagesModel> call, Throwable t) {
                            hideProgress();
                            Toast.makeText(getActivity(), "Network error. Check your connection.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void viewQuotes() {
        showProgress();
        mQuotesArrayList.clear();
        Call<List<QuotesModel>> call = RetrofitClient.getInstance(getActivity())
                .getApiConnector()
                .getQuotes();
        call.enqueue(new Callback<List<QuotesModel>>() {
            @Override
            public void onResponse(Call<List<QuotesModel>> call, Response<List<QuotesModel>> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    mQuotesArrayList.addAll(response.body());
                    quotesAdapter.notifyDataSetChanged();
                }
                else {
                   // Toast.makeText(getActivity(),"Server error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<QuotesModel>> call, Throwable t) {
                hideProgress();
              //  Toast.makeText(getActivity(),"Network error",Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void viewMyQuotes() {
        showProgress();
        mMyQuotesArrayList.clear();
        Call<List<QuotesModel>> call = RetrofitClient.getInstance(getActivity())
                .getApiConnector()
                .getMyQuotes();
        call.enqueue(new Callback<List<QuotesModel>>() {
            @Override
            public void onResponse(Call<List<QuotesModel>> call, Response<List<QuotesModel>> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    mMyQuotesArrayList.addAll(response.body());
                    mm.notifyDataSetChanged();
                }
                else {
                    // Toast.makeText(getActivity(),"Server error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<QuotesModel>> call, Throwable t) {
                hideProgress();
                //  Toast.makeText(getActivity(),"Network error",Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void hideProgress() {
        progressLyt.setVisibility(View.INVISIBLE);
    }

    private void showProgress() {
        progressLyt.setVisibility(View.VISIBLE);
    }

}
