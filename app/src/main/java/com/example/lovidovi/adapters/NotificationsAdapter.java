package com.example.lovidovi.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovidovi.R;
import com.example.lovidovi.models.ReceiveNotificationsModel;

import java.util.ArrayList;

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

    }

    @Override
    public int getItemCount() {
        return mNotificationsArrayList.size();
    }

    public class NotificationsViewHolders extends RecyclerView.ViewHolder {
        TextView notification,sendText;
        ConstraintLayout constraintLayout;
        public NotificationsViewHolders(@NonNull View itemView) {
            super(itemView);
            notification = itemView.findViewById(R.id.notify);
            sendText = itemView.findViewById(R.id.sendText);
            constraintLayout = itemView.findViewById(R.id.constr);
        }
    }
}
