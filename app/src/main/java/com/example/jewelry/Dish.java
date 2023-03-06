package com.example.jewelry;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "dishes")
public class Dish implements Serializable {
    @PrimaryKey @NonNull
    private String id;
    private String category;
    private String name;
    private double price;
    private String icon;
    private String version;

    public Dish(String id, String category, String name, double price, String icon, String version) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.price = price;
        this.icon = icon;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
