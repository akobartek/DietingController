package com.example.przemeksokolowski.dietingcontroller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealWithChoosenProducts {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("meal_type")
    @Expose
    private int mealType;

    @SerializedName("choosen_products")
    @Expose
    private List<ChoosenProductsUsedToGetMeals> choosenProducts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMealType() {
        return mealType;
    }

    public void setMealType(int mealType) {
        this.mealType = mealType;
    }

    public List<ChoosenProductsUsedToGetMeals> getChoosenProducts() {
        return choosenProducts;
    }

    public void setChoosenProducts(List<ChoosenProductsUsedToGetMeals> choosenProducts) {
        this.choosenProducts = choosenProducts;
    }
}
