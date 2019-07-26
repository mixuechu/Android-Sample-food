package com.app.pocketeat.Model;

import com.google.gson.annotations.SerializedName;

public class RestaurantTiming {

    @SerializedName("id")
    private String FoodID;

    @SerializedName("day")
    private String day;

    @SerializedName("mor_open_time")
    private String mor_open_time;

    @SerializedName("mor_close_time")
    private String mor_close_time;

    @SerializedName("eve_open_time")
    private String eve_open_time;

    public String getFoodID() {
        return FoodID;
    }

    public void setFoodID(String foodID) {
        FoodID = foodID;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMor_open_time() {
        return mor_open_time;
    }

    public void setMor_open_time(String mor_open_time) {
        this.mor_open_time = mor_open_time;
    }

    public String getMor_close_time() {
        return mor_close_time;
    }

    public void setMor_close_time(String mor_close_time) {
        this.mor_close_time = mor_close_time;
    }

    public String getEve_open_time() {
        return eve_open_time;
    }

    public void setEve_open_time(String eve_open_time) {
        this.eve_open_time = eve_open_time;
    }

    public String getEve_close_time() {
        return eve_close_time;
    }

    public void setEve_close_time(String eve_close_time) {
        this.eve_close_time = eve_close_time;
    }

    @SerializedName("eve_close_time")
    private String eve_close_time;


}
