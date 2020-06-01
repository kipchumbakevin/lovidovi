package com.example.lovidovi.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovidovi.R;
import com.example.lovidovi.models.ReceiveNotificationsModel;
import com.example.lovidovi.models.SignUpMessagesModel;
import com.example.lovidovi.networking.RetrofitClient;
import com.example.lovidovi.ui.MainActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolders> {

    private Drawable trans;
    private final Context mContext;
    private final ArrayList<ReceiveNotificationsModel> mNotificationsArrayList;
    private final LayoutInflater mLayoutInflator;

    public NotificationsAdapter(Context context, ArrayList<ReceiveNotificationsModel>notificationsArray){
        mContext = context;
        mNotificationsArrayList = notificationsArray;
        trans = mContext.getDrawable(R.color.colorNotViewd);
        mLayoutInflator = LayoutInflater.from(mContext);

    }
    @NonNull
    @Override
    public NotificationsViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.notificationslayout,parent,false);
        return new NotificationsViewHolders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsViewHolders holder, int position) {
        ReceiveNotificationsModel receiveNotificationsModel = mNotificationsArrayList.get(position);
        holder.notification.setText(receiveNotificationsModel.getNotification());
        if (receiveNotificationsModel.getStatus()==0){
            holder.constraintLayout.setBackground(trans);
        }
        holder.phone = receiveNotificationsModel.getSenderPhone();

    }

    @Override
    public int getItemCount() {
        return mNotificationsArrayList.size();
    }

    public class NotificationsViewHolders extends RecyclerView.ViewHolder {
        TextView notification,sendText;
        String phone;
        ConstraintLayout constraintLayout;
        public NotificationsViewHolders(@NonNull View itemView) {
            super(itemView);
            notification = itemView.findViewById(R.id.notify);
            sendText = itemView.findViewById(R.id.sendText);
            constraintLayout = itemView.findViewById(R.id.constr);

            sendText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView cancel, send;
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    View viewx = mLayoutInflator.inflate(R.layout.sendtextfromnoti, null);
                    final EditText message = viewx.findViewById(R.id.messageM);
                    cancel = viewx.findViewById(R.id.cancel);
                    send = viewx.findViewById(R.id.done);

                    alertDialogBuilder.setView(viewx);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    message.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            message.setTextIsSelectable(true);
                            return false;
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (message.getText().toString().isEmpty()) {
                                message.setError("Required");
                            }
                            else {
                                String mess = message.getText().toString();
                                Call<SignUpMessagesModel> call = RetrofitClient.getInstance(mContext)
                                        .getApiConnector()
                                        .sendM(phone, mess);
                                call.enqueue(new Callback<SignUpMessagesModel>() {
                                    @Override
                                    public void onResponse(Call<SignUpMessagesModel> call, Response<SignUpMessagesModel> response) {
                                        if (response.code() == 201) {
                                            Intent intent = new Intent(mContext, MainActivity.class);
                                            mContext.startActivity(intent);
                                            ((Activity) mContext).finish();
                                            alertDialog.dismiss();
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
                        }
                    });
                }
            });
        }
    }
}
