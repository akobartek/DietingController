package com.example.przemeksokolowski.dietingcontroller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductUsedToGetMeals {

    @SerializedName("product[id]")
    @Expose
    private int id;

    @SerializedName("product[name]")
    @Expose
    private String name;

    @SerializedName("product[calories]")
    @Expose
    private int calories;

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
}
