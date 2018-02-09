package com.example.przemeksokolowski.dietingcontroller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChoosenProductsUsedToGetMeals {

    @SerializedName("choosen_product[id]")
    @Expose
    private int id;

    @SerializedName("choosen_product[product_id]")
    @Expose
    private int productId;

    @SerializedName("choosen_product[meal_id]")
    @Expose
    private int mealId;

    @SerializedName("choosen_product[weight]")
    @Expose
    private int weight;

    @SerializedName("choosen_product[product]")
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
