package com.example.przemeksokolowski.dietingcontroller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Workout {

    @SerializedName("activity[id]")
    @Expose
    private int id;

    @SerializedName("activity[time]")
    @Expose
    private int time;

    @SerializedName("activity[activity_type]")
    @Expose
    private WorkoutType workoutType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public WorkoutType getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(WorkoutType workoutType) {
        this.workoutType = workoutType;
    }
}