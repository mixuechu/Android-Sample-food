package com.app.pocketeat.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Restaurant {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("profile_image")
    private String profile_image;

    @SerializedName("bg_image")
    private String bg_image;

    @SerializedName("open_time")
    private String open_time;

    @SerializedName("close_time")
    private String close_time;

    @SerializedName("description")
    private String description;

    @SerializedName("food_type")
    private String food_type;

    @SerializedName("contact_no")
    private String contact_no;

    @SerializedName("address")
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getBg_image() {
        return bg_image;
    }

    public void setBg_image(String bg_image) {
        this.bg_image = bg_image;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<FoodDish> getCuisine_type() {
        return cuisine_type;
    }

    public void setCuisine_type(ArrayList<FoodDish> cuisine_type) {
        this.cuisine_type = cuisine_type;
    }

    @SerializedName("cuisine_type")
    private ArrayList<FoodDish> cuisine_type;

    public ArrayList<RestaurantTiming> getRestaurant_timing() {
        return restaurant_timing;
    }

    public void setRestaurant_timing(ArrayList<RestaurantTiming> restaurant_timing) {
        this.restaurant_timing = restaurant_timing;
    }

    @SerializedName("restaurant_timing")
    private ArrayList<RestaurantTiming> restaurant_timing;

}
