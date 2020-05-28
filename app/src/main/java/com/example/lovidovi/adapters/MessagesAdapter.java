package com.example.lovidovi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovidovi.R;
import com.example.lovidovi.models.ChatsModel;
import com.example.lovidovi.models.MessagesModel;
import com.example.lovidovi.utils.SharedPreferencesConfig;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {
    private final Context mContext;
    private final ArrayList<ChatsModel> mMessagesArrayList;
    private final LayoutInflater mLayoutInflator;

    public MessagesAdapter(Context context, ArrayList<ChatsModel>messagesArray){
        mContext = context;
        mMessagesArrayList = messagesArray;
        mLayoutInflator = LayoutInflater.from(mContext);
    }
    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.chatslayout,parent,false);
        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        SharedPreferencesConfig sharedPreferencesConfig = new SharedPreferencesConfig(mContext);
        ChatsModel chatsModel = mMessagesArrayList.get(position);
        holder.username.setText(chatsModel.getParticipantId());


    }

    @Override
    public int getItemCount() {
        return mMessagesArrayList.size();
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {
        String user;
        TextView username,sample;
        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.sender_username);
            sample = itemView.findViewById(R.id.sample);
        }
    }
}
