package com.app.pocketeat.Adapters;

import android.app.Activity;
import android.app.Notification;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.pocketeat.Model.Notifications;
import com.app.pocketeat.Model.RestaurantTiming;
import com.app.pocketeat.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<Notifications> goalList;
    private Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title,desc,date;


        public MyViewHolder(View view) {
            super(view);

            title=view.findViewById(R.id.title);
            desc=view.findViewById(R.id.desc);
            date=view.findViewById(R.id.txtdate);

        }
    }

    public NotificationAdapter(List<Notifications> goalList, Activity activity) {
        this.goalList = goalList;
        this.activity=activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_notification, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Notifications goalList1 = goalList.get(position);
        holder.title.setText(Html.fromHtml(goalList.get(position).getTitle()));
        holder.desc.setText(Html.fromHtml(goalList1.getDesc()));
        holder.date.setText(goalList1.getCreated_at());

    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

}
