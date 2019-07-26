package com.app.pocketeat.Dashboard;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.app.pocketeat.Adapters.CustomAdapter;
import com.app.pocketeat.Api.ApiClient;
import com.app.pocketeat.Api.ApiInterface;
import com.app.pocketeat.Model.CuisinResponse;
import com.app.pocketeat.Model.Dish;
import com.app.pocketeat.Model.FoodDish;
import com.app.pocketeat.Model.FoodResponse;
import com.app.pocketeat.R;
import com.app.pocketeat.SpannedGridLayoutManager;
import com.app.pocketeat.Utils;
import com.app.pocketeat.databinding.DashboardBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {

    DashboardBinding binding;
    ArrayList<Dish> data=new ArrayList<>();
    ArrayList<FoodDish> cusinList=new ArrayList<>();
    public String sortCuiName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        binding= DataBindingUtil.setContentView(this, R.layout.dashboard);

        if(sortCuiName.trim().length()>0)
        {
            binding.filter.setImageResource(R.drawable.filtered);
        }

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sortCuiName="";
                binding.filter.setImageResource(R.drawable.filter);
                fetchDishList();
            }
        });

        binding.search.clearFocus();
        binding.search.setTextIsSelectable(false);
        binding.search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String searchText=binding.search.getText().toString().trim();
                if(searchText.trim().length()<=0) {
                    if(data.size()>0) {
                        binding.dishList.setVisibility(View.VISIBLE);
                        binding.nodatafound.getRoot().setVisibility(View.GONE);
                        setData(data);
                    }else{
                        binding.dishList.setVisibility(View.GONE);
                        binding.nodatafound.getRoot().setVisibility(View.VISIBLE);
                    }
                }else{
                    ArrayList<Dish> searchedData = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getName().toLowerCase().contains(searchText)) {
                            searchedData.add(data.get(i));
                        }
                    }

                    if (searchedData.size() > 0) {
                        binding.dishList.setVisibility(View.VISIBLE);
                        binding.nodatafound.getRoot().setVisibility(View.GONE);
                        setData(searchedData);
                    } else {
                        binding.dishList.setVisibility(View.GONE);
                        binding.nodatafound.getRoot().setVisibility(View.VISIBLE);
                        binding.nodatafound.message.setText("No search result found");
                    }
                }

            }
        });

        if(Utils.isInternetAvail)
        {
            binding.nodatafound.getRoot().setVisibility(View.GONE);
            fetchDishList();

        }else
        {
            binding.nodatafound.getRoot().setVisibility(View.VISIBLE);
            binding.nodatafound.icIcon.setImageResource(R.drawable.ic_signal_wifi_off_black_24dp);
        }



        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //PopupMenu popup = new PopupMenu(Dashboard.this,binding.filter);
                PopupMenu popup = new PopupMenu(Dashboard.this, v);
                try {

                    for(int i=0;i<cusinList.size();i++) {
                        popup.getMenu().add(cusinList.get(i).getName());
                    }

                    if(cusinList.size()>0) {

                        popup.getMenu().add("Clear Filter");
                    }
                    else{
                        Utils.showToast(Dashboard.this,"No cuisine type available for filter dishes");
                        return;
                    }


                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                                binding.search.clearFocus();
                                ArrayList<Dish> dataFiltered=new ArrayList<>();
                                if(item.getTitle().toString().toLowerCase().equalsIgnoreCase("Clear Filter"))
                                {
                                    sortCuiName="";
                                    setData(data);
                                    binding.filter.setImageResource(R.drawable.filter);
                                }else {
                                    sortCuiName = item.getTitle().toString();
                                    binding.filter.setImageResource(R.drawable.filtered);


                                    for (int i = 0; i < data.size(); i++) {
                                        // data.get(i).setCuisine_type("bar");
                                        if (data.get(i).getCuisine_type().toLowerCase().equals(item.getTitle().toString().toLowerCase())) {
                                            dataFiltered.add(data.get(i));
                                        }
                                    }
                                    if (dataFiltered.size() <= 0) {
                                        binding.dishList.setVisibility(View.GONE);
                                        binding.nodatafound.getRoot().setVisibility(View.VISIBLE);
                                        binding.nodatafound.message.setText("No dish(es) found for filter type " + item.getTitle());
                                    } else {
                                        binding.dishList.setVisibility(View.VISIBLE);
                                        binding.nodatafound.getRoot().setVisibility(View.GONE);
                                        setData(dataFiltered);
                                    }
                                }
                            return false;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.likelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, LikedDishesActivity.class);
                startActivity(intent);
            }
        });

        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ProfileActivity.class);
                startActivity(intent);
            }
        });


    }

    public void fetchDishList()
    {
        //Utils.showProgressBar(Dashboard.this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<FoodResponse> call1 = apiService.getFoodDish();
        call1.enqueue(new Callback<FoodResponse>() {
            @Override
            public void onResponse(Call<FoodResponse> call, final Response<FoodResponse> response) {

                Utils.dismissProgressBar();
                if(response.body().getData()!=null) {
                    if (response.body().getData().size() > 0) {
                        data = response.body().getData();
                        setData(data);

                        fetchCusineType();
                        binding.swipeRefreshLayout.setRefreshing(false);
                    }
                }else{
                    binding.dishList.setVisibility(View.GONE);
                    binding.nodatafound.getRoot().setVisibility(View.VISIBLE);
                    binding.nodatafound.message.setText("No dish(es) found ");
                    fetchCusineType();
                }
             }
                @Override
                public void onFailure(Call<FoodResponse> call, Throwable t) {
                    Utils.dismissProgressBar();
                    call.cancel();
                    binding.nodatafound.getRoot().setVisibility(View.VISIBLE);
                }
            });
    }

    public void fetchCusineType()
    {
        //Utils.showProgressBar(Dashboard.this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CuisinResponse> call1 = apiService.getcuisine_type();
        call1.enqueue(new Callback<CuisinResponse>() {
            @Override
            public void onResponse(Call<CuisinResponse> call, final Response<CuisinResponse> response) {
                Log.d("TEST",""+response.body().getData());
                Utils.dismissProgressBar();
                if(response.body().getData().size()>0)
                {
                    cusinList=response.body().getData();
                }
            }
            @Override
            public void onFailure(Call<CuisinResponse> call, Throwable t) {
                Utils.dismissProgressBar();
                call.cancel();
                binding.nodatafound.getRoot().setVisibility(View.VISIBLE);
            }
        });
    }

    public void setData(final ArrayList<Dish> data) {
        CustomAdapter adapter = new CustomAdapter(data, Dashboard.this);
        binding.dishList.setAdapter(adapter);
        binding.loader.setVisibility(View.GONE);


        if (data.size() > 1) {
            binding.dishList.setLayoutManager(new SpannedGridLayoutManager(
                    new SpannedGridLayoutManager.GridSpanLookup() {
                        @Override
                        public SpannedGridLayoutManager.SpanInfo getSpanInfo(int position) {

                            try {

                                if (data.size() == 1) {
                                    return new SpannedGridLayoutManager.SpanInfo(0, 0);
                                } else {
                                    if (position % 12 == 1 || position % 12 == 6) {
                                        return new SpannedGridLayoutManager.SpanInfo(2, 2);
                                    } else {
                                        return new SpannedGridLayoutManager.SpanInfo(1, 1);
                                    }
                                }

                            } catch (Exception e) {
                                return new SpannedGridLayoutManager.SpanInfo(1, 1);
                            }

                        }
                    },
                    3 /* Three columns */,
                    1f /* We want our items to be 1:1 ratio */));

        } else {
            binding.dishList.setLayoutManager(new GridLayoutManager(this, 2));

        }
    }

}
