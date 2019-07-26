package com.app.pocketeat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pocketeat.Model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    public static Dialog dialog;
    public static User user;
    public static String photoURL = "/Files/PeoplePhoto/";
    public static String attachmentphotoURL = "/Files/ProcessAttachment/";
    public static boolean isInternetAvail=true;


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        alertBuilder.setMessage("External storage permission is necessary");
                    }else{
                        alertBuilder.setMessage("Camera permission is necessary");
                    }
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();


                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                        AlertDialog.Builder alertBuilder1 = new AlertDialog.Builder(context);
                        alertBuilder1.setCancelable(true);
                        alertBuilder1.setTitle("Permission necessary");

                        alertBuilder1.setMessage("Camera permission is necessary");

                        alertBuilder1.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert1 = alertBuilder1.create();
                        alert1.show();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            AlertDialog.Builder alertBuilder1 = new AlertDialog.Builder(context);
                            alertBuilder1.setCancelable(true);
                            alertBuilder1.setTitle("Permission necessary");

                            alertBuilder1.setMessage("Write to disk permission is necessary");

                            alertBuilder1.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                }
                            });
                            AlertDialog alert1 = alertBuilder1.create();
                            alert1.show();
                        } else {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    }
                }

                return false;

            } else {
                return true;
            }


        } else {
            return true;
        }


    }
    public static void showImage(ImageView imgView, String url,Activity activity)
    {
        imgView.setVisibility(View.VISIBLE);

       /* Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .resize(800, 1000)
                .onlyScaleDown()
                .into(imgView);*/

        Glide.with(activity)
                .load(url)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgView);

    }

    public static void showImageBg(ImageView imgView, String url,Activity activity)
    {
        imgView.setVisibility(View.VISIBLE);

        Glide.with(activity)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgView);

    }

    public static void showImageProfile(ImageView imgView, String url)
    {
        imgView.setVisibility(View.VISIBLE);

        Picasso.get()
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imgView);

    }
    public static boolean isNetworkAvailable(Context context) {
        boolean isConnected = true;
        try {
            final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
            isConnected = connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
            isInternetAvail = isConnected;
        }catch (Exception e)
        {

        }
        return isConnected;
    }

    public static Typeface setFont(Context context) {
        Typeface face = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Light.ttf");
        return face;
    }

    public static Typeface setFontButton(Context context) {
        Typeface face = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");
        return face;
    }

    public static Typeface setFontButtonBold(Context context) {
        Typeface face = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Bold.ttf");
        return face;
    }

    public static void hideKeyboard(Activity activity) {
        try {
            View view = activity.findViewById(android.R.id.content);
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
    }

    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }


    public static void clearPreferance(Activity activity) {
        try {
            SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("userList", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            user = null;
        } catch (Exception e) {

        }

    }

    public static void saveUserList(Activity activity, User user) {
        Gson gson = new Gson();
        List<User> userlist=new ArrayList<>();
        if(getUserList(activity)!=null) {
            userlist = getUserList(activity);
        }
        userlist.add(0,user);
        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("userList", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("userDataList", gson.toJson(userlist)); // Storing string
        editor.commit();
    }


    public static void clearCustomField(Activity activity) {
        try {
            SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("customlist", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
        } catch (Exception e) {

        }

    }

    public static List<User> getUserList(Activity activity) {
        Gson gson = new Gson();
        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("userList", 0);
        Type type = new TypeToken<ArrayList<User>>() {}.getType();
        List<User>  userValue = gson.fromJson(pref.getString("userDataList", null), type);
        if(userValue!=null) {
            if (userValue.size() > 0)
                user = userValue.get(0);
        }
        return userValue;
    }

    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void showProgressBar(Context context) {

        dismissProgressBar();

        try {
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progress_custom_dialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void dismissProgressBar() {
        try {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logoutUser(Activity activity) {
        clearPreferance(activity);
        NotificationManager nMgr = (NotificationManager) activity.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
        Utils.clearPreferance(activity);
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    public static final boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }

        return containsDigit;
    }

    public static File getCacheDir(Context context) {
        return context.getCacheDir();
    }

    public static String getNamePic(String inputName, RelativeLayout profilepicture, RelativeLayout namepicture)
    {
        profilepicture.setVisibility(View.GONE);
        namepicture.setVisibility(View.VISIBLE);
        String namepic="";
        String name=inputName.replace("  "," ");
        String[] spillited = name.split(" ");
        try {
            namepic=spillited[0].toString().substring(0, 1) + spillited[spillited.length-1].toString().substring(0, 1);
        }catch (Exception e)
        {
            namepic=spillited[0].toString().substring(0, 1);
        }
        return namepic;
    }

    public static String getNamePicView(String inputName, ImageView profilepicture, TextView namepicture)
    {
        profilepicture.setVisibility(View.GONE);
        namepicture.setVisibility(View.VISIBLE);
        String namepic="";
        String name=inputName.replace("  "," ");
        String[] spillited = name.split(" ");
        try {
            namepic=spillited[0].toString().substring(0, 1) + spillited[spillited.length-1].toString().substring(0, 1);
        }catch (Exception e)
        {
            namepic=spillited[0].toString().substring(0, 1);
        }

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(220), rnd.nextInt(220), rnd.nextInt(220));
        //holder.namepic.setBackgroundColor(color);
        ColorStateList colorclass = new ColorStateList(new int[][]{{}}, new int[]{color});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            namepicture.setBackgroundTintList(colorclass);
        }

        return namepic;
    }
}
