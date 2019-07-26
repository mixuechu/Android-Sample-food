package com.app.pocketeat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.pocketeat.Api.ApiClient;
import com.app.pocketeat.Api.ApiInterface;
import com.app.pocketeat.Dashboard.Dashboard;
import com.app.pocketeat.Model.User;
import com.app.pocketeat.Model.UserResponse;
import com.app.pocketeat.Notifications.NotificationUtils;
import com.app.pocketeat.databinding.LoginActivityBinding;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    LoginActivityBinding binding;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String regId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        binding = DataBindingUtil.setContentView(this, R.layout.login_activity);

        binding.edtpincode.clearFocus();
        binding.edtPhone.clearFocus();

        displayFirebaseRegId();
        try {
            if (regId == null || regId.trim().length() <= 0) {
                mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {

                        // checking for type intent filter
                        if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                            // gcm successfully registered
                            // now subscribe to `global` topic to receive app wide notifications
                            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                            displayFirebaseRegId();

                        } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                            // new push notification is received

                            String message = intent.getStringExtra("message");

                            Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                            //txtMessage.setText(message);
                            Utils.showToast(LoginActivity.this, message);
                        }
                    }
                };
            }

        } catch (Exception e) {
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    // checking for type intent filter
                    if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                        // gcm successfully registered
                        // now subscribe to `global` topic to receive app wide notifications
                        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                        displayFirebaseRegId();

                    } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                        // new push notification is received

                        String message = intent.getStringExtra("message");

                        Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                        //txtMessage.setText(message);
                        Utils.showToast(LoginActivity.this, message);
                    }
                }
            };

        }

        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableSignin();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtpincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableSignin();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.sendpincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.edtPhone.getText().toString().trim().length() <= 0) {
                    Utils.showToast(LoginActivity.this, "Please enter mobile number");
                } else {
                    sendOtp();
                }
            }
        });


        binding.lblSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.leftin, R.anim.leftout);
            }
        });

        binding.btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.isInternetAvail) {
                    login();
                } else {
                    Utils.showToast(LoginActivity.this, "Please try to connect with working internet connection");
                }
            }
        });


    }

    public void login() {
        Utils.showProgressBar(LoginActivity.this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        String android_id = Settings.Secure.getString(LoginActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (android_id == null)
            android_id = "sdm636";
        else if (android_id.trim().length() <= 0)
            android_id = "sdm636";


        if (regId == null)
            regId = "null";
        else if (regId.trim().length() <= 0)
            regId = "null";


        Call<UserResponse> call1 = apiService.login(binding.edtPhone.getText().toString().trim(), binding.edtpincode.getText().toString().trim(), android_id, "0", regId);
        call1.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, final Response<UserResponse> response) {

                Utils.dismissProgressBar();
                if (response.body() != null) {
                    if (response.body().isResult() == 0) {
                        Log.d("TEST", "" + response.body().getUserInfo());
                        if (response.body().getUserInfo() != null) {
                            Utils.saveUserList(LoginActivity.this, response.body().getUserInfo());
                            Utils.user = response.body().getUserInfo();
                            Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Utils.showToast(LoginActivity.this, response.body().getMessage());
                        }

                    } else {
                        Utils.showToast(LoginActivity.this, response.body().getMessage());
                    }
                } else {
                    Utils.showToast(LoginActivity.this, "Unable to connect server");
                }

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Utils.showToast(LoginActivity.this, "Unable to connect server");
                Utils.dismissProgressBar();
                call.cancel();
            }
        });
    }

    public void sendOtp() {
        Utils.showProgressBar(LoginActivity.this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        Call<UserResponse> call1 = apiService.sendOtp(binding.edtPhone.getText().toString().trim());
        call1.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, final Response<UserResponse> response) {

                Utils.dismissProgressBar();
                Utils.showToast(LoginActivity.this, response.body().getMessage());
                binding.edtpincode.requestFocus();

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Utils.showToast(LoginActivity.this, "Unable to connect server");
                Utils.dismissProgressBar();
                call.cancel();
            }
        });
    }

    public void enableSignin() {
        if (binding.edtPhone.getText().toString().trim().length() > 0 && binding.edtpincode.getText().toString().trim().length() > 0) {
            binding.btncontinue.setEnabled(true);
        } else {
            binding.btncontinue.setEnabled(false);
        }
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);
/*
        Log.e("TEST", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            //txtRegId.setText("Firebase Reg Id: " + regId);
            Utils.showToast(LoginScreen.this,""+regId);
        else
            Utils.showToast(LoginScreen.this,"Not register");*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

}
