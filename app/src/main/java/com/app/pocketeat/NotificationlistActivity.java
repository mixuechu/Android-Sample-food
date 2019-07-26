package com.app.pocketeat;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.app.pocketeat.Adapters.DishAdapter;
import com.app.pocketeat.Adapters.NotificationAdapter;
import com.app.pocketeat.Api.ApiClient;
import com.app.pocketeat.Api.ApiInterface;
import com.app.pocketeat.Model.Dish;
import com.app.pocketeat.Model.LikedDishList;
import com.app.pocketeat.Model.NotificationResponse;
import com.app.pocketeat.Model.Notifications;
import com.app.pocketeat.R;
import com.app.pocketeat.databinding.LikedDishesBinding;
import com.app.pocketeat.databinding.NotificationsBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationlistActivity extends AppCompatActivity {

    NotificationsBinding binding;
    public static int isUpdated=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);
        binding= DataBindingUtil.setContentView(this, R.layout.notifications);


        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Utils.isNetworkAvailable(NotificationlistActivity.this))
                {
                    binding.nodatafound.getRoot().setVisibility(View.GONE);
                    binding.loader.setVisibility(View.VISIBLE);
                    binding.rvnotification.setVisibility(View.GONE);
                    fetchNotification();

                }else
                {
                    binding.nodatafound.getRoot().setVisibility(View.VISIBLE);
                    binding.loader.setVisibility(View.GONE);
                    binding.rvnotification.setVisibility(View.GONE);
                    binding.nodatafound.message.setText("No working internet connection found");
                }
            }
        });
        if(Utils.isNetworkAvailable(NotificationlistActivity.this))
        {
            binding.nodatafound.getRoot().setVisibility(View.GONE);
            binding.loader.setVisibility(View.VISIBLE);
            binding.rvnotification.setVisibility(View.GONE);
            fetchNotification();

        }else
        {
            binding.nodatafound.getRoot().setVisibility(View.VISIBLE);
            binding.loader.setVisibility(View.GONE);
            binding.rvnotification.setVisibility(View.GONE);
            binding.nodatafound.message.setText("No working internet connection found");
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

    public void fetchNotification()
    {
        //Utils.showProgressBar(LikedDishesActivity.this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<NotificationResponse> call1 = apiService.notificationlist(Utils.user.getUsers_token());
        call1.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, final Response<NotificationResponse> response) {

                if(response.body()!=null) {
                    if(response.body().getUserInfo()!=null) {
                        if (response.body().getUserInfo().size() > 0) {
                            binding.nodatafound.getRoot().setVisibility(View.GONE);
                            binding.loader.setVisibility(View.GONE);

                            binding.rvnotification.setVisibility(View.VISIBLE);
                            List<Notifications> data = new ArrayList<>();
                            data = response.body().getUserInfo();
                            isUpdated=0;
                            NotificationAdapter adapter = new NotificationAdapter(data, NotificationlistActivity.this);
                            binding.rvnotification.setAdapter(adapter);

                        }else {
                            binding.nodatafound.getRoot().setVisibility(View.VISIBLE);
                            binding.loader.setVisibility(View.GONE);
                            binding.rvnotification.setVisibility(View.GONE);
                            binding.nodatafound.message.setText("No notifications found");
                        }
                    }else{
                        binding.nodatafound.getRoot().setVisibility(View.VISIBLE);
                        binding.loader.setVisibility(View.GONE);
                        binding.rvnotification.setVisibility(View.GONE);
                        binding.nodatafound.message.setText("No notifications found");
                    }
                }else{
                    binding.nodatafound.getRoot().setVisibility(View.VISIBLE);
                    binding.loader.setVisibility(View.GONE);
                    binding.rvnotification.setVisibility(View.GONE);
                    binding.nodatafound.message.setText("No notifications found");
                }

            }
            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                //Utils.dismissProgressBar();
                call.cancel();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
