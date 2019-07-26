package com.app.pocketeat.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.pocketeat.Dashboard.RestaurantDetailActivity;
import com.app.pocketeat.Model.FoodDish;
import com.app.pocketeat.R;
import com.app.pocketeat.Utils;

import java.util.List;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.MyViewHolder> {

    private List<FoodDish> goalList;
    private Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivImage;
        TextView title,desc;
        LinearLayout llmain;

        public MyViewHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.ivImage);
            title=view.findViewById(R.id.title);
            desc=view.findViewById(R.id.desc);
            llmain=view.findViewById(R.id.llmainview);
        }
    }

    public FeaturedAdapter(List<FoodDish> goalList, Activity activity) {
        this.goalList = goalList;
        this.activity=activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_featured, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FoodDish goalList1 = goalList.get(position);
        if(goalList1.getName()!=null)
            holder.title.setText(Html.fromHtml(goalList1.getName()));

        Utils.showImage(holder.ivImage, goalList1.getFood_type_img(),activity);
        if(goalList1.getDesc()==null) {
            holder.desc.setVisibility(View.GONE);
        }else{
            if(goalList1.getDesc().trim().length()<=0)
            {
                holder.desc.setVisibility(View.GONE);
            }else{
                holder.desc.setText(Html.fromHtml(goalList1.getDesc()));
            }
        }


    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

}
