package com.app.pocketeat.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CuisinResponse {

    @SerializedName("status")
    private int status;

    public int isStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<FoodDish> getData() {
        return data;
    }

    public void setData(ArrayList<FoodDish> data) {
        this.data = data;
    }

    @SerializedName("data")
    private ArrayList<FoodDish> data;

}
