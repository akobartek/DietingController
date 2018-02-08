package com.example.przemeksokolowski.dietingcontroller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChoosenProductsUsedToGetMeals {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("product_id")
    @Expose
    private int productId;

    @SerializedName("meal_id")
    @Expose
    private int mealId;

    @SerializedName("weight")
    @Expose
    private int weight;

    @SerializedName("product")
    @Expose
    private ProductUsedToGetMeals product;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public ProductUsedToGetMeals getProduct() {
        return product;
    }

    public void setProduct(ProductUsedToGetMeals product) {
        this.product = product;
    }
}
