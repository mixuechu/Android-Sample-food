package com.app.pocketeat.Dashboard;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import com.app.pocketeat.Api.ApiClient;
import com.app.pocketeat.Api.ApiInterface;

import com.app.pocketeat.LoginActivity;
import com.app.pocketeat.Model.RestaurantDetailResponse;
import com.app.pocketeat.Model.UserResponse;
import com.app.pocketeat.R;
import com.app.pocketeat.Utils;
import com.app.pocketeat.databinding.EditprofileBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    EditprofileBinding binding;
    String userChoosenTask;
    public int REQUEST_CAMERA = 10;
    public int SELECT_FILE = 11;
    File f;
    public static int isUpdated=0;
    Bitmap thumbnail;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    public int REQUEST_PERMISSIONS_CODE_WRITE_STORAGE = 100;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);
        binding= DataBindingUtil.setContentView(this, R.layout.editprofile);

        binding.edtpincode.clearFocus();
        binding.edtPhone.clearFocus();
        binding.edyusername.clearFocus();

        binding.edyusername.setText(Utils.user.getName());
        binding.name.setText(Utils.user.getName());
        binding.edtPhone.setText(Utils.user.getPhno());

        binding.edyusername.setSelection(binding.edyusername.getText().length());
        binding.edtPhone.setSelection(binding.edtPhone.getText().length());
        Utils.showImageProfile(binding.ivProfile,Utils.user.getProfile_image());
        if (!checkPermissions(EditProfileActivity.this, permissions)) {
            // Check Permissions Now
            requestPermissions( //Method of Fragment
                    permissions,
                    REQUEST_PERMISSIONS_CODE_WRITE_STORAGE
            );
        }


        binding.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
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

        binding.edyusername.addTextChangedListener(new TextWatcher() {
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
                if(binding.edtPhone.getText().toString().trim().length()<=0)
                {
                    Utils.showToast(EditProfileActivity.this,"Please enter mobile number");
                }else{
                    sendOtp();
                }
            }
        });


        binding.btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateProfile();

            }
        });
    }

    public void sendOtp() {
        Utils.showProgressBar(EditProfileActivity.this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        Call<UserResponse> call1 = apiService.verifyOTP(Utils.user.getUsers_token(),binding.edtPhone.getText().toString().trim());
        call1.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, final Response<UserResponse> response) {

                Utils.dismissProgressBar();
                Utils.showToast(EditProfileActivity.this, response.body().getMessage());
                binding.edtpincode.requestFocus();

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Utils.showToast(EditProfileActivity.this, "Unable to connect server");
                Utils.dismissProgressBar();
                call.cancel();
            }
        });
    }

    public void updateProfile()
    {
        Utils.showProgressBar(EditProfileActivity.this);
        MultipartBody.Part filebody = null;
        if (f != null) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), f);

            filebody =
                    MultipartBody.Part.createFormData("profile", f.getName() + ".png", requestFile);
        }
        RequestBody name = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(binding.edyusername.getText().toString().trim()));

        RequestBody verify_code = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(binding.edtpincode.getText().toString().trim()));

        RequestBody number = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(binding.edtPhone.getText().toString().trim()));

        RequestBody token = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(Utils.user.getUsers_token()));

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<RestaurantDetailResponse> call1 = apiService.saveProfile(name,number,token,verify_code,filebody);
        call1.enqueue(new Callback<RestaurantDetailResponse>() {
            @Override
            public void onResponse(Call<RestaurantDetailResponse> call, final Response<RestaurantDetailResponse> response) {

                Utils.dismissProgressBar();
                if(response.body()!=null) {
                    if (response.body().isStatus()== 1) {
                        isUpdated=1;

                        finish();

                    }else{
                        Utils.showToast(EditProfileActivity.this, response.body().getMessage());
                    }
                }else{
                    Utils.showToast(EditProfileActivity.this, "Unable to connect server");
                }

            }
            @Override
            public void onFailure(Call<RestaurantDetailResponse> call, Throwable t) {
                Utils.showToast(EditProfileActivity.this, "Unable to connect server");
                Utils.dismissProgressBar();
                call.cancel();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (!checkPermissions(EditProfileActivity.this, permissions)) {
                    // Check Permissions Now
                    requestPermissions( //Method of Fragment
                            permissions,
                            REQUEST_PERMISSIONS_CODE_WRITE_STORAGE
                    );
                }else{
                    if (items[item].equals("Take Photo")) {
                        userChoosenTask = "Take Photo";

                            cameraIntent();
                    } else if (items[item].equals("Choose from Library")) {
                        userChoosenTask = "Choose from Library";

                            galleryIntent();
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }


            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    public void enableSignin()
    {
        if(binding.edyusername.getText().toString().trim().length()>0 && binding.edtPhone.getText().toString().trim().length()>0)
        {
            if(!binding.edtPhone.getText().toString().trim().equalsIgnoreCase(Utils.user.getPhno()))
            {
                binding.btncontinue.setEnabled(false);
            }else{
                binding.btncontinue.setEnabled(true);
            }

        }else {
            binding.btncontinue.setEnabled(false);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) try {
            bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            f = new File(getCacheDir(), String.valueOf(System.currentTimeMillis()));
            f.createNewFile();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            binding.ivProfile.setImageBitmap(bm);
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static boolean checkPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null &&
                permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 90, bytes);
        binding.ivProfile.setImageBitmap(thumbnail);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".png");
        FileOutputStream fo;
        try {

            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            f = destination;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
