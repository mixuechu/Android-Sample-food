package com.app.pocketeat.Model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    private String PersonID;

    public String getPersonID() {
        return PersonID;
    }

    public void setPersonID(String personID) {
        PersonID = personID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("name")
    private String name;

    public String getUsers_token() {
        return users_token;
    }

    public void setUsers_token(String users_token) {
        this.users_token = users_token;
    }

    @SerializedName("users_token")
    private String users_token;

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    @SerializedName("mobile")
    private String phno;

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    @SerializedName("profile_image")
    private String profile_image;

}
