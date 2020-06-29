package com.example.lovidovi.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovidovi.R;
import com.example.lovidovi.models.QuotesModel;
import com.example.lovidovi.models.SignUpMessagesModel;
import com.example.lovidovi.networking.RetrofitClient;
import com.example.lovidovi.ui.MainActivity;
import com.example.lovidovi.utils.SharedPreferencesConfig;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyQuotesAdapter extends RecyclerView.Adapter<MyQuotesAdapter.QuotesViewHolder> {
        private final Context mContext;
        private final ArrayList<QuotesModel> mMyQuotesArrayList;
        private final LayoutInflater mLayoutInflator;

    public MyQuotesAdapter(Context context, ArrayList<QuotesModel>quotesArray){
            mContext = context;
        mMyQuotesArrayList = quotesArray;
            mLayoutInflator = LayoutInflater.from(mContext);

        }
        @NonNull
        @Override
        public MyQuotesAdapter.QuotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mLayoutInflator.inflate(R.layout.quotesview,parent,false);
            return new QuotesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyQuotesAdapter.QuotesViewHolder holder, int position) {
            QuotesModel quotesModel = mMyQuotesArrayList.get(position);
            SharedPreferencesConfig sharedPreferencesConfig = new SharedPreferencesConfig(mContext);
            holder.id = Integer.toString(quotesModel.getId());
            if (Integer.toString(quotesModel.getSenderId()).equals(sharedPreferencesConfig.readClientsName())){
                holder.delete.setVisibility(View.VISIBLE);
            }
            holder.quote.setText(quotesModel.getQuote());

        }

        @Override
        public int getItemCount() {
            return mMyQuotesArrayList.size();
        }

        public class QuotesViewHolder extends RecyclerView.ViewHolder {
            TextView quote;
            ImageView share,delete;
            String id;
            public QuotesViewHolder(@NonNull View itemView) {
                super(itemView);
                quote = itemView.findViewById(R.id.viewQuote);
                share = itemView.findViewById(R.id.share);
                delete = itemView.findViewById(R.id.deletequote);

                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        String shareBody = quote.getText().toString();
                        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        mContext.startActivity(Intent.createChooser(intent, "Share via"));
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("Delete")
                                .setMessage("Are you sure you want to delete?")
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Call<SignUpMessagesModel> call = RetrofitClient.getInstance(mContext)
                                                .getApiConnector()
                                                .deleteQuote(id);
                                        call.enqueue(new Callback<SignUpMessagesModel>() {
                                            @Override
                                            public void onResponse(Call<SignUpMessagesModel> call, Response<SignUpMessagesModel> response) {
                                                if (response.code() == 201) {
                                                    Intent intent = new Intent(mContext, MainActivity.class);
                                                    mContext.startActivity(intent);
                                                    ((Activity) mContext).finish();
                                                    Toast.makeText(mContext,"Deleted successfully", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(mContext, "Server error", Toast.LENGTH_LONG).show();
                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<SignUpMessagesModel> call, Throwable t) {
                                                Toast.makeText(mContext, t.getMessage() + "error", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                });
            }
    }
}
