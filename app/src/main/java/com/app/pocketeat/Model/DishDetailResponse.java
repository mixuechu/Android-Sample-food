package com.app.pocketeat.Model;

import com.google.gson.annotations.SerializedName;

public class DishDetailResponse {

    @SerializedName("status")
    private int status;

    public int isStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Dish getData() {
        return data;
    }

    public void setData(Dish data) {
        this.data = data;
    }

    @SerializedName("data")
    private Dish data;


}
