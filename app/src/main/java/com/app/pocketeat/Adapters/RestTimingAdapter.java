package com.app.pocketeat.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.pocketeat.Dashboard.RestaurantDetailActivity;
import com.app.pocketeat.Model.FoodDish;
import com.app.pocketeat.Model.RestaurantTiming;
import com.app.pocketeat.R;
import com.app.pocketeat.Utils;

import java.util.List;

public class RestTimingAdapter extends RecyclerView.Adapter<RestTimingAdapter.MyViewHolder> {

    private List<RestaurantTiming> goalList;
    private Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title,desc;


        public MyViewHolder(View view) {
            super(view);

            title=view.findViewById(R.id.title);
            desc=view.findViewById(R.id.desc);

        }
    }

    public RestTimingAdapter(List<RestaurantTiming> goalList, Activity activity) {
        this.goalList = goalList;
        this.activity=activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_timing, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final RestaurantTiming goalList1 = goalList.get(position);
        holder.title.setText(goalList1.getDay());

        if(goalList1.getMor_close_time()!=null && goalList1.getMor_close_time()!=null && goalList1.getEve_open_time()!=null &&  goalList1.getEve_close_time()!=null)
        {
            if(goalList1.getMor_close_time().trim().length()>0 && goalList1.getMor_close_time().trim().length()>0 && goalList1.getEve_open_time().length()>0 &&  goalList1.getEve_close_time().length()>0) {
                holder.desc.setText(goalList1.getMor_open_time() + " to " + goalList1.getMor_close_time() + "\n" + goalList1.getEve_open_time() + " to " + goalList1.getEve_close_time());
            }else if(goalList1.getMor_close_time().trim().length()>0 && goalList1.getMor_close_time().trim().length()>0){
                holder.desc.setText(goalList1.getMor_open_time() + " to " + goalList1.getMor_close_time());
            }else if(goalList1.getEve_close_time().trim().length()>0 && goalList1.getEve_open_time().trim().length()>0){
                holder.desc.setText(goalList1.getEve_open_time() + " to " + goalList1.getEve_close_time());
            }else if(goalList1.getMor_open_time().trim().length()>0 && goalList1.getEve_close_time().trim().length()>0) {
                holder.desc.setText(goalList1.getMor_open_time() + " to " + goalList1.getEve_close_time());
            }else if(goalList1.getEve_close_time().trim().length()>0) {
                holder.desc.setText(" Closes at " + goalList1.getEve_close_time());
            }else if(goalList1.getMor_open_time().trim().length()>0) {
                holder.desc.setText(" Opens at " + goalList1.getMor_open_time());
            }else{
                holder.desc.setText(" - ");
            }


        }else {
            if(goalList1.getMor_open_time()!=null && goalList1.getEve_close_time()!=null) {
                holder.desc.setText(goalList1.getMor_open_time() + " to " + goalList1.getEve_close_time());
            }
        }

    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

}
