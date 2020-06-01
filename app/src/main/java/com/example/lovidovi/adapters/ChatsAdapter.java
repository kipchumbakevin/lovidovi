package com.example.lovidovi.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovidovi.R;
import com.example.lovidovi.models.ChatsModel;
import com.example.lovidovi.ui.MessagesActivity;
import com.example.lovidovi.utils.SharedPreferencesConfig;

import java.util.ArrayList;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MessagesViewHolder> {
    private static final String KNOW = "com.example.lovidovi.adapters";
    private final Context mContext;
    private final ArrayList<ChatsModel> mMessagesArrayList;
    private final LayoutInflater mLayoutInflator;

    public ChatsAdapter(Context context, ArrayList<ChatsModel>messagesArray){
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
        if (chatsModel.getParticipant()== null && !chatsModel.getParticipantId().equals(sharedPreferencesConfig.readClientsName())){
            holder.username.setText(chatsModel.getParticipantId());
            holder.pp = chatsModel.getParticipantId();
        }
        if (chatsModel.getParticipantId().equals(sharedPreferencesConfig.readClientsName())&&chatsModel.getOwner()!=null)
        {
            holder.username.setText(chatsModel.getOwner().getUsername());
            holder.pp = chatsModel.getOwner().getPhone();
        }
        if (chatsModel.getParticipantId().equals(sharedPreferencesConfig.readClientsName())&&chatsModel.getOwner()==null)
        {
            holder.username.setText(chatsModel.getOwnerId());
        }
        if(chatsModel.getParticipant()!=null && !chatsModel.getParticipantId().equals(sharedPreferencesConfig.readClientsName()) ) {
            holder.username.setText(chatsModel.getParticipant().getUsername());
            holder.sample.setText(chatsModel.getParticipant().getPhone());
            holder.pp = chatsModel.getParticipant().getPhone();
        }

        holder.mcurrentposition = chatsModel.getId();
        holder.id = Integer.toString(holder.mcurrentposition);
    }

    @Override
    public int getItemCount() {
        return mMessagesArrayList.size();
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {
        int mcurrentposition;
        String id,tt,pp;
        TextView username,sample;
        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.sender_username);
            sample = itemView.findViewById(R.id.sample);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MessagesActivity.class);
                    intent.putExtra("CHATID",id);
                    intent.putExtra("USERNAME",username.getText().toString());
                    intent.putExtra(KNOW,true);
                    intent.putExtra("PHONE",pp);
                    mContext.startActivity(intent);
                 //   ((Activity) mContext).finish();
                }
            });
        }
    }
}