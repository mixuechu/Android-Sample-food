package com.app.pocketeat.Model;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

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

    @SerializedName("data")
    private User userInfo;

    public int isResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }

}
