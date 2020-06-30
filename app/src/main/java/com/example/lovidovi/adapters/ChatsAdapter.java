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
import com.example.lovidovi.models.ChatsModel;
import com.example.lovidovi.models.SignUpMessagesModel;
import com.example.lovidovi.networking.RetrofitClient;
import com.example.lovidovi.ui.MainActivity;
import com.example.lovidovi.ui.MessagesActivity;
import com.example.lovidovi.utils.SharedPreferencesConfig;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MessagesViewHolder> {
    private static final String KNOW = "com.example.lovidovi.adapters";
    private final Context mContext;
    private final boolean mmm = false;
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
        holder.r = Integer.toString(chatsModel.getOwnerDelete());
        if (chatsModel.getReceiver()!=null && chatsModel.getReceiver().getReceiverId().equals(sharedPreferencesConfig.readClientsName()) && chatsModel.getReceiver().getReceiverRead()==0){
            holder.unread.setVisibility(View.VISIBLE);
        }
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
        String id,tt,pp,ss,r;
        TextView username,sample;
        ImageView unread;
        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.sender_username);
            sample = itemView.findViewById(R.id.sample);
            unread = itemView.findViewById(R.id.unreadmessage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    unread.setVisibility(View.GONE);
                    Intent intent = new Intent(mContext, MessagesActivity.class);
                    intent.putExtra("CHATID",id);
                    intent.putExtra("USERNAME",username.getText().toString());
                    intent.putExtra(KNOW,true);
                    intent.putExtra("PHONE",pp);
                    intent.putExtra("DEL",r);
                    mContext.startActivity(intent);
                 //   ((Activity) mContext).finish();
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Delete")
                            .setMessage("Delete messages in this chat?")
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
                                            .deleteChat(id);
                                    call.enqueue(new Callback<SignUpMessagesModel>() {
                                        @Override
                                        public void onResponse(Call<SignUpMessagesModel> call, Response<SignUpMessagesModel> response) {
                                            if (response.code() == 201) {
                                                Intent intent = new Intent(mContext, MainActivity.class);
                                                mContext.startActivity(intent);
                                                ((Activity) mContext).finish();
                                                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
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
                    return false;
                }
            });
        }
    }
}
