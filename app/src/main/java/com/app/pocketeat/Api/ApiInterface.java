package com.app.pocketeat.Api;


import com.app.pocketeat.Model.CuisinResponse;
import com.app.pocketeat.Model.DishDetailResponse;
import com.app.pocketeat.Model.FoodResponse;
import com.app.pocketeat.Model.LikedDishList;
import com.app.pocketeat.Model.NotificationResponse;
import com.app.pocketeat.Model.RestaurantDetailResponse;
import com.app.pocketeat.Model.UserResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiInterface {
/*
    @FormUrlEncoded
    @POST("VerifyDomain")
    Call<User> verifyDomain(@Field("Domain") String domain);*/

    /*@GET("admin/food_dish_list")
    Call<Dish> getFoodDish(@Query("email") String emailID);*/


    @GET("admin/food_dish_list")
    Call<FoodResponse> getFoodDish();

    @GET("admin/cuisine_type")
    Call<CuisinResponse> getcuisine_type();

    @FormUrlEncoded
    @POST("admin/favourite_food_dish")
    Call<LikedDishList> getDishList(@Field("users_token") String users_token);

    @FormUrlEncoded
    @POST("admin/food_dish_detail")
    Call<DishDetailResponse> getDishDetail(@Field("users_token") String users_token, @Field("id") String id);

    @FormUrlEncoded
    @POST("admin/send_otp")
    Call<UserResponse> sendOtp(@Field("mobile") String users_token);

    @FormUrlEncoded
    @POST("admin/send_otp")
    Call<UserResponse> verifyOTP(@Field("users_token") String users_token,@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("admin/food_dish_like")
    Call<DishDetailResponse> likeDish(@Field("users_token") String users_token, @Field("dish_id") String dish_id);

    @FormUrlEncoded
    @POST("admin/restaurant_detail")
    Call<RestaurantDetailResponse> getRestaurantDetail(@Field("id") String id);

    @Multipart
    @POST("admin/edit_user_account")
    Call<RestaurantDetailResponse> saveProfile(@Part("name") RequestBody name
            , @Part("mobile") RequestBody mobile
            , @Part("users_token") RequestBody users_token
            , @Part("verification_code") RequestBody verification_code
            ,@Part MultipartBody.Part image);

    @Multipart
    @POST("admin/sign_up")
    Call<RestaurantDetailResponse> sign_up(@Part("name") RequestBody name
            , @Part("location") RequestBody location
            , @Part("mobile") RequestBody mobile
            ,@Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("admin/login")
    Call<UserResponse> login(@Field("mobile") String mobile,@Field("password") String pin,@Field("device_id") String deviceid,@Field("device_type") String devicetype,@Field("fcm_token") String fcm_type);

    @FormUrlEncoded
    @POST("admin/logout")
    Call<UserResponse> logout(@Field("users_token") String users_token);


    @FormUrlEncoded
    @POST("admin/user_account")
    Call<UserResponse> user_account(@Field("users_token") String users_token);

    @FormUrlEncoded
    @POST("admin/notification_list")
    Call<NotificationResponse> notificationlist(@Field("users_token") String users_token);


    //image tag is profile

}