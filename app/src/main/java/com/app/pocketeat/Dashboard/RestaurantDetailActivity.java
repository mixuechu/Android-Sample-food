package com.app.pocketeat.Dashboard;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.pocketeat.Adapters.FeaturedAdapter;
import com.app.pocketeat.Adapters.RestTimingAdapter;
import com.app.pocketeat.Adapters.ViewPagerAdapter;
import com.app.pocketeat.Api.ApiClient;
import com.app.pocketeat.Api.ApiInterface;
import com.app.pocketeat.Model.Restaurant;
import com.app.pocketeat.Model.RestaurantDetailResponse;
import com.app.pocketeat.Model.RestaurantTiming;
import com.app.pocketeat.R;
import com.app.pocketeat.Utils;
import com.app.pocketeat.databinding.RestaurantDetailBinding;

import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetailActivity extends AppCompatActivity {

    RestaurantDetailBinding binding;
    String id="0";
    Restaurant restairantDetail;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    String[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_detail);
        binding= DataBindingUtil.setContentView(this, R.layout.restaurant_detail);
        id=getIntent().getStringExtra("id");

        fetchRestaurantDetail();


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.txtcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=restairantDetail.getContact_no().toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+number));
                startActivity(callIntent);
            }
        });

    }

    public void fetchRestaurantDetail()
    {
        Utils.showProgressBar(RestaurantDetailActivity.this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<RestaurantDetailResponse> call1 = apiService.getRestaurantDetail(id);
        call1.enqueue(new Callback<RestaurantDetailResponse>() {
            @Override
            public void onResponse(Call<RestaurantDetailResponse> call, final Response<RestaurantDetailResponse> response) {
                Log.d("TEST",""+response.body().getData());

                restairantDetail=response.body().getData();
                Utils.dismissProgressBar();
                if(restairantDetail!=null) {
                    binding.txtname.setText(Html.fromHtml(restairantDetail.getName()));
                    binding.txtdesc.setText(Html.fromHtml(restairantDetail.getDescription()));
                    binding.txtcontact.setText(restairantDetail.getContact_no());
                    binding.txtaddress.setText(Html.fromHtml(restairantDetail.getAddress()));
                    Utils.showImage(binding.ivProfile, restairantDetail.getProfile_image(),RestaurantDetailActivity.this);
                    //Utils.showImage(binding.bgImage, restairantDetail.getBg_image());
                    images=restairantDetail.getBg_image().split(",");
                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(RestaurantDetailActivity.this,true,images);

                    binding.viewPager.setAdapter(viewPagerAdapter);

                    if(images.length>1) {
                        dotscount = viewPagerAdapter.getCount();
                        dots = new ImageView[dotscount];

                        for (int i = 0; i < dotscount; i++) {

                            dots[i] = new ImageView(RestaurantDetailActivity.this);
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            params.setMargins(8, 0, 8, 0);

                            binding.sliderdots.addView(dots[i], params);

                        }

                        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

                        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {

                                for (int i = 0; i < dotscount; i++) {
                                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                                }

                                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                    }

                    binding.txttype.setText(restairantDetail.getFood_type());
                    binding.txtopenhours.setText(restairantDetail.getOpen_time() + " - " + restairantDetail.getClose_time());

                    Bitmap bm=((BitmapDrawable)binding.bgImage.getDrawable()).getBitmap();
                    Blurry.with(RestaurantDetailActivity.this).from(bm).into(binding.bgImage);
                    if(restairantDetail.getCuisine_type()!=null) {
                        FeaturedAdapter adapter = new FeaturedAdapter(restairantDetail.getCuisine_type(), RestaurantDetailActivity.this);
                        binding.featured.setLayoutManager(new LinearLayoutManager(RestaurantDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        binding.featured.setAdapter(adapter);
                    }

                    if(restairantDetail.getRestaurant_timing().size()>0) {

                        RestTimingAdapter timeadapter = new RestTimingAdapter(restairantDetail.getRestaurant_timing(), RestaurantDetailActivity.this);
                        binding.rvtiming.setLayoutManager(new LinearLayoutManager(RestaurantDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                        binding.rvtiming.setAdapter(timeadapter);
                    }else{
                        ArrayList<RestaurantTiming> data = new ArrayList<>();
                        for(int i=0;i<=6;i++) {
                            RestaurantTiming timing = new RestaurantTiming();
                            if(i==0)
                                timing.setDay("Mon");
                            else if(i==1)
                                timing.setDay("Tue");
                            else if(i==2)
                                timing.setDay("Wed");
                            else if(i==3)
                                timing.setDay("Thu");
                            else if(i==4)
                                timing.setDay("Fri");
                            else if(i==5)
                                timing.setDay("Sat");
                            else if(i==6)
                                timing.setDay("Sun");

                            timing.setMor_open_time(restairantDetail.getOpen_time());
                            timing.setEve_close_time(restairantDetail.getClose_time());

                            data.add(timing);
                        }

                        RestTimingAdapter timeadapter = new RestTimingAdapter(data, RestaurantDetailActivity.this);
                        binding.rvtiming.setLayoutManager(new LinearLayoutManager(RestaurantDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                        binding.rvtiming.setAdapter(timeadapter);

                    }
                }else {
                    Utils.showToast(RestaurantDetailActivity.this,"No restaurant details found");
                }

            }
            @Override
            public void onFailure(Call<RestaurantDetailResponse> call, Throwable t) {
                Utils.dismissProgressBar();
                call.cancel();
            }
        });
    }
}
