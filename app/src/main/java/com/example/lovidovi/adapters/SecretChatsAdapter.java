package com.example.lovidovi.adapters;

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

public class SecretChatsAdapter extends RecyclerView.Adapter<SecretChatsAdapter.SecretViewHolder> {
    private final Context mContext;
    private final ArrayList<ChatsModel> mSecretArrayList;
    private final LayoutInflater mLayoutInflator;

    public SecretChatsAdapter(Context context, ArrayList<ChatsModel>secretmessages){
        mContext = context;
        mSecretArrayList =secretmessages;
        mLayoutInflator = LayoutInflater.from(mContext);
    }
    @NonNull
    @Override
    public SecretChatsAdapter.SecretViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.secret_chats,parent,false);
        return new SecretViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SecretChatsAdapter.SecretViewHolder holder, int position) {
        SharedPreferencesConfig sharedPreferencesConfig = new SharedPreferencesConfig(mContext);
        ChatsModel chatsModel = mSecretArrayList.get(position);
        if (chatsModel.getParticipant()== null && !chatsModel.getParticipantId().equals(sharedPreferencesConfig.readClientsName())){
            holder.username.setText(chatsModel.getParticipantId());
            holder.pp = chatsModel.getParticipantId();
        }
        if (chatsModel.getParticipantId().equals(sharedPreferencesConfig.readClientsName())&&chatsModel.getOwner()!=null)
        {
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
        return mSecretArrayList.size();
    }

    public class SecretViewHolder extends RecyclerView.ViewHolder {
        int mcurrentposition;
        String id;
        TextView username,sample;
        String pp;
        public SecretViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.sender_username);
            sample = itemView.findViewById(R.id.sample);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MessagesActivity.class);
                    intent.putExtra("CHATID",id);
                    intent.putExtra("SIMU",pp);
                    intent.putExtra("USERNAME",username.getText().toString());
                    mContext.startActivity(intent);
                    //   ((Activity) mContext).finish();
                }
            });
        }
    }
}