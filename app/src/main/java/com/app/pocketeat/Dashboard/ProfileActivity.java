package com.app.pocketeat.Dashboard;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.app.pocketeat.Api.ApiClient;
import com.app.pocketeat.Api.ApiInterface;

import com.app.pocketeat.LoginActivity;
import com.app.pocketeat.Model.UserResponse;
import com.app.pocketeat.NotificationlistActivity;
import com.app.pocketeat.R;
import com.app.pocketeat.Utils;
import com.app.pocketeat.databinding.ProfileBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    ProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        binding= DataBindingUtil.setContentView(this, R.layout.profile);
        if(Utils.isNetworkAvailable(ProfileActivity.this))
        {
            getData();

        }else
        {
            binding.txtusername.setText(Utils.user.getName());
            binding.txtphoneno.setText(Utils.user.getPhno());
            Utils.showImageProfile(binding.profile,Utils.user.getProfile_image());
        }

        binding.editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, NotificationlistActivity.class);
                startActivity(intent);
            }
        });

        binding.changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        binding.txtqrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, QRCodeActivity.class);
                startActivity(intent);
            }
        });

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


        binding.btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               logout();
            }
        });

    }

    public void logout()
    {
        Utils.showProgressBar(ProfileActivity.this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String android_id = Settings.Secure.getString(ProfileActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if(android_id==null)
            android_id="sdm636";
        else if(android_id.trim().length()<=0)
            android_id="sdm636";


        Call<UserResponse> call1 = apiService.logout(Utils.user.getUsers_token());
        call1.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, final Response<UserResponse> response) {

                Utils.dismissProgressBar();
                Utils.logoutUser(ProfileActivity.this);
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    public void getData()
    {
        Utils.showProgressBar(ProfileActivity.this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        Call<UserResponse> call1 = apiService.user_account(Utils.user.getUsers_token());
        call1.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, final Response<UserResponse> response) {

                Utils.dismissProgressBar();
                if(response.body()!=null) {
                    if (response.body().isResult() == 0) {
                        Log.d("TEST", "" + response.body().getUserInfo());

                        Utils.clearPreferance(ProfileActivity.this);
                        Utils.saveUserList(ProfileActivity.this, response.body().getUserInfo());
                        Utils.user = response.body().getUserInfo();

                        binding.txtusername.setText(Utils.user.getName());
                        binding.txtphoneno.setText(Utils.user.getPhno());
                        Utils.showImageProfile(binding.profile,Utils.user.getProfile_image());

                    } else {
                        Utils.showToast(ProfileActivity.this, "Invalid phone number/ pincode");
                    }
                }else{
                    Utils.showToast(ProfileActivity.this, "Unable to connect server");
                }

            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Utils.showToast(ProfileActivity.this, "Unable to connect server");
                Utils.dismissProgressBar();
                call.cancel();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(EditProfileActivity.isUpdated==1) {
            EditProfileActivity.isUpdated=0;
            getData();
        }
    }
}
