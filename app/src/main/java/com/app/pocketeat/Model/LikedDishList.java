package com.app.pocketeat.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LikedDishList {

    @SerializedName("status")
    private int status;

    public int isStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Dish> getData() {
        return data;
    }

    public void setData(ArrayList<Dish> data) {
        this.data = data;
    }

    @SerializedName("data")
    private ArrayList<Dish> data;

}
