package com.example.przemeksokolowski.dietingcontroller.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChoosenProducts {

    public ChoosenProducts(int productId, int mealId, int weight) {
        this.productId = productId;
        this.mealId = mealId;
        this.weight = weight;
    }

    @SerializedName("choosen_product[product_id]")
    @Expose
    private int productId;

    @SerializedName("choosen_product[meal_id]")
    @Expose
    private int mealId;

    @SerializedName("choosen_product[weight]")
    @Expose
    private int weight;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}