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

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private String[] images;
    boolean isOverlap;

    public ViewPagerAdapter(Context context,boolean isOverlap,String[] images) {
        this.context = context;
        this.isOverlap=isOverlap;
        this.images=images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.imagesliderview, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        //imageView.setImageResource(images[position]);

        ImageView overlap = (ImageView) view.findViewById(R.id.overlap);

        if(isOverlap) {
            Utils.showImageBg(imageView,images[position],(Activity) context);
            overlap.setVisibility(View.VISIBLE);
        }
        else {
            Utils.showImage(imageView,images[position],(Activity) context);
            overlap.setVisibility(View.GONE);
        }

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}