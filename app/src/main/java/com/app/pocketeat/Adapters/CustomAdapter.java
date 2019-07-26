package com.app.pocketeat.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.pocketeat.Dashboard.DishDetail;
import com.app.pocketeat.Model.Dish;
import com.app.pocketeat.R;
import com.app.pocketeat.Utils;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<Dish> goalList;
    private Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivImage;
        LinearLayout llmain;
        TextView badge;

        public MyViewHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.ivImage);
            llmain=view.findViewById(R.id.llmain);
            badge=view.findViewById(R.id.badge);
        }
    }

    public CustomAdapter(List<Dish> goalList, Activity activity) {
        this.goalList = goalList;
        this.activity=activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fooddish, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Dish goalList1 = goalList.get(position);

        Utils.showImage(holder.ivImage, goalList1.getDish_image(),activity);

        if(goalList1.getIs_label_exist().equalsIgnoreCase("0"))
        {
            holder.badge.setVisibility(View.GONE);
        }else {
            holder.badge.setVisibility(View.VISIBLE);
            ColorStateList csl = new ColorStateList(new int[][]{{}}, new int[]{Color.parseColor(goalList1.getColor())});
            holder.badge.setBackgroundTintList(csl);
            holder.badge.setText(goalList1.getLabel_description());

        }

        if(goalList1.getLabel_description().length()>4)
            holder.badge.setTextSize(8);
        else
            holder.badge.setTextSize(10);

        holder.llmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DishDetail.class);
                intent.putExtra("id",goalList1.getFoodID());
                activity.startActivity(intent);
            }
        });

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DishDetail.class);
                intent.putExtra("id",goalList1.getFoodID());
                activity.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

}
