package com.app.pocketeat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.app.pocketeat.Api.ApiClient;
import com.app.pocketeat.Api.ApiInterface;
import com.app.pocketeat.Dashboard.Dashboard;

import com.app.pocketeat.Dashboard.ProfileActivity;
import com.app.pocketeat.Model.RestaurantDetailResponse;
import com.app.pocketeat.Model.UserResponse;
import com.app.pocketeat.databinding.RegisterActivityBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignupActivity extends AppCompatActivity {

    RegisterActivityBinding binding;
    File f;
    Bitmap thumbnail;
    String location="";
    String userChoosenTask;
    public int REQUEST_CAMERA = 10;
    public int SELECT_FILE = 11;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    public int REQUEST_PERMISSIONS_CODE_WRITE_STORAGE = 100;
    PlacesTask placesTask;
    ParserTask parserTask;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        binding= DataBindingUtil.setContentView(this, R.layout.register_activity);

        binding.edtpincode.clearFocus();
        binding.edtPhone.clearFocus();
        binding.edyusername.clearFocus();
        if (!checkPermissions(SignupActivity.this, permissions)) {
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


        binding.edtCurrentCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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


        binding.sendpincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.edtPhone.getText().toString().trim().length()<=0)
                {
                    Utils.showToast(SignupActivity.this,"Please enter mobile number");
                }else{
                    sendOtp();
                }
            }
        });



        binding.lblSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(SignupActivity.this, Dashboard.class);
                startActivity(intent);
                finish();*/
                signupData();
            }
        });


    }

    public void sendOtp()
    {
        Utils.showProgressBar(SignupActivity.this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        Call<UserResponse> call1 = apiService.sendOtp(binding.edtPhone.getText().toString().trim());
        call1.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, final Response<UserResponse> response) {

                Utils.dismissProgressBar();
                Utils.showToast(SignupActivity.this, response.body().getMessage());
                binding.edtpincode.requestFocus();

            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Utils.showToast(SignupActivity.this, "Unable to connect server");
                Utils.dismissProgressBar();
                call.cancel();
            }
        });
    }

    public void signupData()
    {
        Utils.showProgressBar(SignupActivity.this);
        location=binding.edtCurrentCity.getText().toString();
        if(location==null)
        {
            Utils.showToast(SignupActivity.this,"Please enter a location");
        }else{
            if(location.trim().length()<=0)
            {
                Utils.showToast(SignupActivity.this,"Please enter a location");
            }
        }
        MultipartBody.Part filebody = null;
        if (f != null) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), f);

            filebody =
                    MultipartBody.Part.createFormData("profile_image", f.getName() + ".png", requestFile);
        }else
        {
            Utils.showToast(SignupActivity.this,"Please select profile picture");
            Utils.dismissProgressBar();
            return;
        }
        RequestBody name = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(binding.edyusername.getText().toString().trim()));

        RequestBody location1 = RequestBody.create(
                MediaType.parse("text/plain"),
                location);

        RequestBody number = RequestBody.create(
                MediaType.parse("text/plain"),
                String.valueOf(binding.edtPhone.getText().toString().trim()));

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<RestaurantDetailResponse> call1 = apiService.sign_up(name,location1,number,filebody);
        call1.enqueue(new Callback<RestaurantDetailResponse>() {
            @Override
            public void onResponse(Call<RestaurantDetailResponse> call, final Response<RestaurantDetailResponse> response) {

                Utils.dismissProgressBar();

                if(response.body()!=null) {
                    Utils.showToast(SignupActivity.this,response.body().getMessage());
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Utils.showToast(SignupActivity.this, "Unable to connect server");
                }

            }
            @Override
            public void onFailure(Call<RestaurantDetailResponse> call, Throwable t) {
                Utils.showToast(SignupActivity.this, "Unable to connect server");
                Utils.dismissProgressBar();
                call.cancel();
            }
        });
    }
    
    public void enableSignin()
    {
        if(binding.edyusername.getText().toString().trim().length()>0 && binding.edtPhone.getText().toString().trim().length()>0)
        {
            binding.btncontinue.setEnabled(true);
        }else {
            binding.btncontinue.setEnabled(false);
        }
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
                if (!checkPermissions(SignupActivity.this, permissions)) {
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

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyBXHac5vbfqCHvMhaDYlXdwBf_JDOCNxcg";

            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }


            // place type to be searched
            String types = "types=geocode&components=country:ca";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input + "&" + types + "&" + sensor + "&" + key;

            // Output formaGeocoder t
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                // Fetching the data from web service in background
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[]{"description"};
            int[] to = new int[]{android.R.id.text1};

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter

            binding.edtCurrentCity.setAdapter(adapter);



        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
