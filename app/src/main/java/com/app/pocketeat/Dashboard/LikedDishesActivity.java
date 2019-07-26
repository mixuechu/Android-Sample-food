package com.app.pocketeat.Dashboard;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.app.pocketeat.Adapters.DishAdapter;
import com.app.pocketeat.Api.ApiClient;
import com.app.pocketeat.Api.ApiInterface;
import com.app.pocketeat.Model.Dish;
import com.app.pocketeat.Model.LikedDishList;
import com.app.pocketeat.R;
import com.app.pocketeat.Utils;
import com.app.pocketeat.databinding.LikedDishesBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikedDishesActivity extends AppCompatActivity {

    LikedDishesBinding binding;
    public static int isUpdated=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liked_dishes);
        binding= DataBindingUtil.setContentView(this, R.layout.liked_dishes);


        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Utils.isNetworkAvailable(LikedDishesActivity.this))
                {
                    binding.loader.setVisibility(View.VISIBLE);
                    binding.nodatafound.getRoot().setVisibility(View.GONE);
                    fetchDishList();

                }else
                {
                    binding.loader.setVisibility(View.GONE);
                    binding.nodatafound.getRoot().setVisibility(View.VISIBLE);
                }
            }
        });
        if(Utils.isNetworkAvailable(LikedDishesActivity.this))
        {
            binding.loader.setVisibility(View.VISIBLE);
            binding.nodatafound.getRoot().setVisibility(View.GONE);
            fetchDishList();

        }else
        {
            binding.loader.setVisibility(View.GONE);
            binding.nodatafound.getRoot().setVisibility(View.VISIBLE);
        }
        binding.title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() <= (binding.title.getLeft() + binding.title.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        // your action here
                        finish();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void fetchDishList()
    {
        //Utils.showProgressBar(LikedDishesActivity.this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<LikedDishList> call1 = apiService.getDishList(Utils.user.getUsers_token());
        call1.enqueue(new Callback<LikedDishList>() {
            @Override
            public void onResponse(Call<LikedDishList> call, final Response<LikedDishList> response) {
                Log.d("TEST",""+response.body().getData());
                //Utils.dismissProgressBar();
                binding.loader.setVisibility(View.GONE);
                if(response.body()!=null) {
                    if(response.body().getData()!=null) {
                        if (response.body().getData().size() > 0) {
                            binding.dishList.setVisibility(View.VISIBLE);
                            ArrayList<Dish> data = new ArrayList<>();
                            data = response.body().getData();
                            isUpdated=0;
                            DishAdapter adapter = new DishAdapter(data, LikedDishesActivity.this);
                            binding.dishList.setAdapter(adapter);

                            GridLayoutManager manager = new GridLayoutManager(LikedDishesActivity.this, 2, GridLayoutManager.VERTICAL, false);
                            binding.dishList.setLayoutManager(manager);

                        }else{
                            binding.nodatafound.getRoot().setVisibility(View.VISIBLE);
                        }
                    }else{
                        binding.nodatafound.getRoot().setVisibility(View.VISIBLE);
                    }
                }else{
                    binding.nodatafound.getRoot().setVisibility(View.VISIBLE);
                }
                binding.nodatafound.message.setText("No dish(ed) are found in your favorite list!");
                binding.swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<LikedDishList> call, Throwable t) {
                //Utils.dismissProgressBar();
                call.cancel();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isUpdated==1) {
            isUpdated=0;
            fetchDishList();
        }
    }
}
