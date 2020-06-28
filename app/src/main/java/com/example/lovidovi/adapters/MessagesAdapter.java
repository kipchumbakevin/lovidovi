package com.example.lovidovi.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Layout;
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
import com.example.lovidovi.models.MessagesModel;
import com.example.lovidovi.models.SignUpMessagesModel;
import com.example.lovidovi.networking.RetrofitClient;
import com.example.lovidovi.ui.MainActivity;
import com.example.lovidovi.utils.SharedPreferencesConfig;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {
    private final Context mContext;
    private final ArrayList<MessagesModel> mMessagesArrayList;
    private final LayoutInflater mLayoutInflator;

    public MessagesAdapter(Context context, ArrayList<MessagesModel>messagesArray){
        mContext = context;
        mMessagesArrayList = messagesArray;
        mLayoutInflator = LayoutInflater.from(mContext);
    }
    @NonNull
    @Override
    public MessagesAdapter.MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.messageslayout,parent,false);
        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MessagesViewHolder holder, int position) {
        SharedPreferencesConfig sharedPreferencesConfig = new SharedPreferencesConfig(mContext);
        MessagesModel messagesModel = mMessagesArrayList.get(position);
        holder.iii = Integer.toString(messagesModel.getSenderId());
        holder.id = Integer.toString(messagesModel.getId());
        if (holder.iii.equals(sharedPreferencesConfig.readClientsName())){
            holder.sent.setVisibility(View.VISIBLE);
            holder.received.setVisibility(View.GONE);
            holder.sent.setText(messagesModel.getMessage());
            if (messagesModel.getReceiverRead()==1){
                holder.read.setVisibility(View.VISIBLE);
            }else {
                holder.alreadysent.setVisibility(View.VISIBLE);
            }
        }
        else{
            holder.received.setVisibility(View.VISIBLE);
            holder.read.setVisibility(View.GONE);
            holder.alreadysent.setVisibility(View.GONE);
            holder.sent.setVisibility(View.GONE);
            holder.received.setText(messagesModel.getMessage());
        }



    }

    @Override
    public int getItemCount() {
        return mMessagesArrayList.size();
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {
        String iii,id;
        ImageView alreadysent,read;
        TextView received,sent;
        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            received = itemView.findViewById(R.id.received_text);
            sent = itemView.findViewById(R.id.sent_text);
            alreadysent = itemView.findViewById(R.id.senttext);
            read = itemView.findViewById(R.id.readtext);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Delete")
                            .setMessage("You can only delete this chat. Messages will be deleted on the next update. ")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {


                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return false;
                }
            });
        }
    }
}
