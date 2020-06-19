package com.example.lovidovi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovidovi.R;
import com.example.lovidovi.models.QuotesModel;

import java.util.ArrayList;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.QuotesViewHolder> {
    private final Context mContext;
    private final ArrayList<QuotesModel> mQuotesArrayList;
    private final LayoutInflater mLayoutInflator;

    public QuotesAdapter(Context context, ArrayList<QuotesModel>quotesArray){
        mContext = context;
        mQuotesArrayList = quotesArray;
        mLayoutInflator = LayoutInflater.from(mContext);

    }
    @NonNull
    @Override
    public QuotesAdapter.QuotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.quotesview,parent,false);
        return new QuotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuotesAdapter.QuotesViewHolder holder, int position) {
        QuotesModel quotesModel = mQuotesArrayList.get(position);
        holder.quote.setText(quotesModel.getQuote());

    }

    @Override
    public int getItemCount() {
        return mQuotesArrayList.size();
    }

    public class QuotesViewHolder extends RecyclerView.ViewHolder {
        TextView quote;
        ImageView share;
        public QuotesViewHolder(@NonNull View itemView) {
            super(itemView);
            quote = itemView.findViewById(R.id.viewQuote);
            share = itemView.findViewById(R.id.share);

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
        }
    }
}
