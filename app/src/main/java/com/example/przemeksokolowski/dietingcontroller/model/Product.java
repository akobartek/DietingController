package com.example.przemeksokolowski.dietingcontroller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("calories")
    @Expose
    private int calories;

    @SerializedName("verified")
    @Expose
    private boolean verified;

    public Product(int id, String name, int calories, boolean verified) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.verified = verified;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean varified) {
        this.verified = verified;
    }
}