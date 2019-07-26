package com.app.pocketeat.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.MyViewHolder> {

    private List<Dish> goalList;
    private Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivImage;
        TextView title,desc;
        LinearLayout llmain;
        TextView badge;
        public MyViewHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.ivImage);
            title=view.findViewById(R.id.title);
            desc=view.findViewById(R.id.desc);
            llmain=view.findViewById(R.id.llmainview);
            badge=view.findViewById(R.id.badge);
        }
    }

    public DishAdapter(List<Dish> goalList, Activity activity) {
        this.goalList = goalList;
        this.activity=activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_dish, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Dish goalList1 = goalList.get(position);

        if(goalList1!=null) {
            if (goalList1.getName() != null) {
                if (goalList1.getName().trim().length() > 0) {
                    holder.title.setText(Html.fromHtml(goalList1.getName()));
                    holder.title.setVisibility(View.VISIBLE);
                } else {
                    holder.title.setVisibility(View.GONE);
                }

                if (goalList1.getDesc() != null && goalList1.getDesc().trim().length() > 0) {
                    holder.desc.setText(Html.fromHtml(goalList1.getDesc()));
                    holder.desc.setVisibility(View.VISIBLE);
                } else {
                    holder.desc.setVisibility(View.GONE);
                }
            } else {
                holder.title.setVisibility(View.INVISIBLE);
                holder.desc.setVisibility(View.INVISIBLE);
            }

            if(goalList1.getIs_label_exist()!=null) {
                if (goalList1.getIs_label_exist().equalsIgnoreCase("0")) {
                    holder.badge.setVisibility(View.GONE);
                } else {
                    holder.badge.setVisibility(View.VISIBLE);
                    if(goalList1.getColor()!=null && goalList1.getColor().length()>0) {
                        ColorStateList csl = new ColorStateList(new int[][]{{}}, new int[]{Color.parseColor(goalList1.getColor())});
                        holder.badge.setBackgroundTintList(csl);
                    }
                    if(goalList1.getLabel_description()!=null) {
                        holder.badge.setText(Html.fromHtml(goalList1.getLabel_description()));
                    }

                }
            }else{
                holder.badge.setVisibility(View.GONE);
            }

            String[] images=goalList1.getDish_image().split(",");
            if(images.length>0)
                Utils.showImage(holder.ivImage,images[0],activity);
            else
                Utils.showImage(holder.ivImage,goalList1.getDish_image(),activity);
        }


        holder.llmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goalList1!=null) {
                    Intent intent = new Intent(activity, DishDetail.class);
                    intent.putExtra("id", goalList1.getFoodID());
                    activity.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

}
