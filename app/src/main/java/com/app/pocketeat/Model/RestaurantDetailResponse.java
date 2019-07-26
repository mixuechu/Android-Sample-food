package com.app.pocketeat.Model;

import com.google.gson.annotations.SerializedName;

public class RestaurantDetailResponse {

    @SerializedName("status")
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    private String message;

    public int isStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Restaurant getData() {
        return data;
    }

    public void setData(Restaurant data) {
        this.data = data;
    }

    @SerializedName("data")
    private Restaurant data;

}
