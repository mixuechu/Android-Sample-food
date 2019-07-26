package com.app.pocketeat.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationResponse {

    @SerializedName("result")
    private int result;

    public int getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    private String message;

    @SerializedName("date")
    private List<Notifications> userInfo;

    public int isResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<Notifications> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(List<Notifications> userInfo) {
        this.userInfo = userInfo;
    }

}
