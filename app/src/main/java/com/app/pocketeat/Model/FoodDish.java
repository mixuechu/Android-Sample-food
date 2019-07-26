package com.app.pocketeat.Model;

import com.google.gson.annotations.SerializedName;

public class FoodDish {

    @SerializedName("id")
    private String FoodID;

    @SerializedName("name")
    private String name;

    public String getFoodID() {
        return FoodID;
    }

    public void setFoodID(String foodID) {
        FoodID = foodID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFood_type_img() {
        return food_type_img;
    }

    public void setFood_type_img(String food_type_img) {
        this.food_type_img = food_type_img;
    }

    @SerializedName("food_type_img")
    private String food_type_img;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @SerializedName("desc")
    private String desc;

}
