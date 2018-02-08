package com.example.przemeksokolowski.dietingcontroller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkoutType {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("burned_calories")
    @Expose
    private int burnedCalories;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBurnedCalories() {
        return burnedCalories;
    }

    public void setBurnedCalories(int burnedCalories) {
        this.burnedCalories = burnedCalories;
    }
}