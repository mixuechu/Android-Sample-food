package com.app.pocketeat.Dashboard;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.pocketeat.Adapters.ViewPagerAdapter;
import com.app.pocketeat.Api.ApiClient;
import com.app.pocketeat.Api.ApiInterface;
import com.app.pocketeat.Model.Dish;
import com.app.pocketeat.Model.DishDetailResponse;
import com.app.pocketeat.R;
import com.app.pocketeat.Utils;
import com.app.pocketeat.databinding.DishDetailBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DishDetail extends AppCompatActivity {

    DishDetailBinding binding;
    String id="0";
    Dish dishDetail;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    String[] images= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dish_detail);
        binding= DataBindingUtil.setContentView(this, R.layout.dish_detail);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        id=getIntent().getStringExtra("id");
        binding.txtrestname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DishDetail.this, RestaurantDetailActivity.class);
                intent.putExtra("id",dishDetail.getRestaurant_id());
                startActivity(intent);
            }
        });
        fetchDishList();

        binding.ivshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Utils.showProgressBar(DishDetail.this);

                try {
                    Utils.showProgressBar(DishDetail.this);
                    images = dishDetail.getDish_image().split(",");

                    File f = new File(getCacheDir(), String.valueOf(System.currentTimeMillis()));
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap image = null;
                    try {
                        URL url = new URL(images[0]);
                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                        byte[] bitmapdata = bos.toByteArray();
                        FileOutputStream fos = new FileOutputStream(f);

                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();

                        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), image, "title", null);
                        Uri bitmapUri = Uri.parse(bitmapPath);

                        Intent shareIntent = new Intent();
                        String shareBody = "Hello, I had already liked " + dishDetail.getName() + " from " + dishDetail.getRestaurant_name() + "\n" + "Please try once you would loved it!\nThank you!!!";
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                        shareIntent.setType("image/*");
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivityForResult(Intent.createChooser(shareIntent, "send"),100);

                    } catch (IOException e) {
                        System.out.println(e);
                    }




                    // Utils.dismissProgressBar();
                }catch (Exception e)
                {
                    Intent shareIntent = new Intent();
                    String shareBody = "Hello, I had already liked " + dishDetail.getName() + " from " + dishDetail.getRestaurant_name() + "\n" + "Please try once you would loved it!\nThank you!!!";
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    shareIntent.setType("text/plain");
                    startActivityForResult(Intent.createChooser(shareIntent, "send"),100);
                }



            }
        });
        binding.llbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikedDishesActivity.isUpdated=1;
                if(dishDetail.getAlredy_like()!=null) {
                    if(dishDetail.getAlredy_like().equalsIgnoreCase("1"))
                    {
                        //Utils.showToast(DishDetail.this,"This Dish is already liked by you!");
                    }else {
                        likeDish();
                    }
                }else{
                    likeDish();
                }
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void fetchDishList()
    {
        Utils.showProgressBar(DishDetail.this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<DishDetailResponse> call1 = apiService.getDishDetail(Utils.user.getUsers_token(),id);
        call1.enqueue(new Callback<DishDetailResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<DishDetailResponse> call, final Response<DishDetailResponse> response) {
                Log.d("TEST",""+response.body().getData());
                Utils.dismissProgressBar();

                dishDetail=response.body().getData();
                binding.txtname.setText(Html.fromHtml("<HTML>"+dishDetail.getName()+" | "+"<font color='#30A0E5'>"+dishDetail.getDish_price()+"</font></HTML>"));

                binding.txtrate.setVisibility(View.GONE);
                binding.txtrestname.setText(Html.fromHtml("#"+dishDetail.getRestaurant_name()));
                binding.txtrestdesc.setText(Html.fromHtml(dishDetail.getRestaurant_description()));

                //binding.txtrestname.getPaint().setShader(new LinearGradient(0,0,binding.txtrestname.getLineHeight(),0, Color.parseColor("#26A5E7"), Color.parseColor("#CE5AC9"), Shader.TileMode.REPEAT));
                TextPaint paint = binding.txtrestname.getPaint();
                float width = paint.measureText(binding.txtrestname.getText().toString());

                Shader textShader = new LinearGradient(0, 0, width, binding.txtrestname.getTextSize(),
                        new int[]{
                                Color.parseColor("#26A5E7"),
                                Color.parseColor("#7880D8"),
                                Color.parseColor("#CE5AC9"),

                        }, null, Shader.TileMode.CLAMP);
                binding.txtrestname.getPaint().setShader(textShader);

                if(dishDetail.getLabel_description().length()>4)
                    binding.badge.setTextSize(8);
                images=dishDetail.getDish_image().split(",");
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(DishDetail.this,false,images);

                binding.viewPager.setAdapter(viewPagerAdapter);

                if(images.length>1) {
                    dotscount = viewPagerAdapter.getCount();
                    dots = new ImageView[dotscount];

                    for (int i = 0; i < dotscount; i++) {

                        dots[i] = new ImageView(DishDetail.this);
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

                binding.expandTextView.setText(dishDetail.getDesc());

                binding.likecount.setText(dishDetail.getTotal_likes()+" Liked");
               // Utils.showImage(binding.ivpicture,dishDetail.getDish_image());
                Utils.showImage(binding.ivpic,dishDetail.getFood_type_img(),DishDetail.this);


                if(dishDetail.getIs_label_exist().equalsIgnoreCase("0"))
                {
                    binding.badge.setVisibility(View.GONE);
                }else {
                    binding.badge.setVisibility(View.VISIBLE);
                    ColorStateList csl = new ColorStateList(new int[][]{{}}, new int[]{Color.parseColor(dishDetail.getColor())});
                    binding.badge.setBackgroundTintList(csl);
                    binding.badge.setText(dishDetail.getLabel_description());

                }

                if(dishDetail.getAlredy_like()==null)
                {
                    binding.llbg.getBackground().setColorFilter(Color.parseColor("#30A0E5"), PorterDuff.Mode.SRC_ATOP);
                    binding.like.setBackgroundResource(R.drawable.like);
                }else {
                    if (dishDetail.getAlredy_like().equalsIgnoreCase("1")) {
                        binding.llbg.getBackground().setColorFilter(Color.parseColor("#CB69CF"), PorterDuff.Mode.SRC_ATOP);
                        binding.like.setBackgroundResource(R.drawable.liked);
                        binding.like.getBackground().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);

                    } else {
                        binding.llbg.getBackground().setColorFilter(Color.parseColor("#30A0E5"), PorterDuff.Mode.SRC_ATOP);
                        binding.like.setBackgroundResource(R.drawable.like);
                    }
                }
            }
            @Override
            public void onFailure(Call<DishDetailResponse> call, Throwable t) {
                Utils.dismissProgressBar();
                call.cancel();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.dismissProgressBar();
    }

    public void likeDish()
    {
        Utils.showProgressBar(DishDetail.this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<DishDetailResponse> call1 = apiService.likeDish(Utils.user.getUsers_token(),dishDetail.getFoodID());
        call1.enqueue(new Callback<DishDetailResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<DishDetailResponse> call, final Response<DishDetailResponse> response) {
                try {

                    if (dishDetail.getAlredy_like() == null || dishDetail.getAlredy_like().equalsIgnoreCase("0")) {
                        binding.like.setBackgroundResource(R.drawable.liked);
                        binding.like.getBackground().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                        try {
                            binding.likecount.setText((Integer.parseInt(dishDetail.getTotal_likes()) + 1) + " Liked");
                            dishDetail.setTotal_likes(String.valueOf(Integer.parseInt(dishDetail.getTotal_likes()) + 1));
                        }catch (Exception e)
                        {
                            binding.likecount.setText("1 Liked");
                            dishDetail.setTotal_likes(String.valueOf("1"));
                        }

                        dishDetail.setAlredy_like("1");
                        binding.llbg.getBackground().setColorFilter(Color.parseColor("#CB69CF"), PorterDuff.Mode.SRC_ATOP);
                    } else {
                        binding.like.setBackgroundResource(R.drawable.like);
                        binding.likecount.setText((Integer.parseInt(dishDetail.getTotal_likes()) - 1) + " Liked");
                        dishDetail.setTotal_likes(String.valueOf(Integer.parseInt(dishDetail.getTotal_likes()) - 1));
                        dishDetail.setAlredy_like("0");
                        binding.llbg.getBackground().setColorFilter(Color.parseColor("#30A0E5"), PorterDuff.Mode.SRC_ATOP);
                    }
                }catch (Exception e)
                {
                    binding.like.setBackgroundResource(R.drawable.liked);
                    binding.like.getBackground().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                    try {
                        binding.likecount.setText((Integer.parseInt(dishDetail.getTotal_likes()) + 1) + " Liked");
                        dishDetail.setTotal_likes(String.valueOf(Integer.parseInt(dishDetail.getTotal_likes()) + 1));
                    }catch (Exception e1)
                    {
                        binding.likecount.setText("1 Liked");
                        dishDetail.setTotal_likes(String.valueOf("1"));
                    }
                    dishDetail.setAlredy_like("1");
                    binding.llbg.getBackground().setColorFilter(Color.parseColor("#CB69CF"), PorterDuff.Mode.SRC_ATOP);
                }

                Utils.dismissProgressBar();

            }
            @Override
            public void onFailure(Call<DishDetailResponse> call, Throwable t) {
                Utils.dismissProgressBar();
                call.cancel();
            }
        });
    }


}
