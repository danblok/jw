package com.example.jewelry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DishResponse {

    @SerializedName("dishes")
    private List<Dish> dishes;

    public DishResponse(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    @Override
    public String toString() {
        return "DishResponse{" +
                "dishes=" + dishes +
                '}';
    }
}
