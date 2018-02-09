package com.example.przemeksokolowski.dietingcontroller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkoutType {

    @SerializedName("activity_type[id]")
    @Expose
    private int id;

    @SerializedName("activity_type[name]")
    @Expose
    private String name;

    @SerializedName("activity_type[burned_calories]")
    @Expose
    private int burnedCalories;

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

    public int getBurnedCalories() {
        return burnedCalories;
    }

    public void setBurnedCalories(int burnedCalories) {
        this.burnedCalories = burnedCalories;
    }
}