package com.app.pocketeat.Model;

import com.google.gson.annotations.SerializedName;

public class Dish {

    @SerializedName("id")
    private String FoodID;

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

    @SerializedName("dish_name")
    private String name;

    @SerializedName("restaurant_name")
    private String restaurant_name;

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getRestaurant_description() {
        return restaurant_description;
    }

    public void setRestaurant_description(String restaurant_description) {
        this.restaurant_description = restaurant_description;
    }

    public String getFood_type_img() {
        return food_type_img;
    }

    public void setFood_type_img(String food_type_img) {
        this.food_type_img = food_type_img;
    }

    @SerializedName("restaurant_description")
    private String restaurant_description;

    @SerializedName("food_type_img")
    private String food_type_img;

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    @SerializedName(value="Desc", alternate={"dish_description"})
    private String Desc;

    @SerializedName("dish_price")
    private String dish_price;

    public String getDish_price() {
        return dish_price;
    }

    public void setDish_price(String dish_price) {
        this.dish_price = dish_price;
    }

    public String getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(String total_likes) {
        this.total_likes = total_likes;
    }

    @SerializedName("total_likes")
    private String total_likes;

    @SerializedName("restaurant_id")
    private String restaurant_id;

    @SerializedName("dish_image")
    private String dish_image;

    @SerializedName("is_label_exist")
    private String is_label_exist;

    @SerializedName("label_id")
    private String label_id;

    @SerializedName("cuisine_type")
    private String cuisine_type;

    public String getLabel_description() {
        return label_description;
    }

    public void setLabel_description(String label_description) {
        this.label_description = label_description;
    }

    @SerializedName("label_description")
    private String label_description;

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getDish_image() {
        return dish_image;
    }

    public void setDish_image(String dish_image) {
        this.dish_image = dish_image;
    }

    public String getIs_label_exist() {
        return is_label_exist;
    }

    public void setIs_label_exist(String is_label_exist) {
        this.is_label_exist = is_label_exist;
    }

    public String getLabel_id() {
        return label_id;
    }

    public void setLabel_id(String label_id) {
        this.label_id = label_id;
    }

    public String getCuisine_type() {
        return cuisine_type;
    }

    public void setCuisine_type(String cuisine_type) {
        this.cuisine_type = cuisine_type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAlredy_like() {
        return alredy_like;
    }

    public void setAlredy_like(String alredy_like) {
        this.alredy_like = alredy_like;
    }

    @SerializedName("color")
    private String color;

    @SerializedName("alredy_like")
    private String alredy_like;

}
