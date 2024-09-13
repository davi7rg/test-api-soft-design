package org.softdesign.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductResponse {

    @JsonProperty("id")
    private int id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("price")
    private int price;
    @JsonProperty("discountPercentage")
    private double discountPercentage;
    @JsonProperty("rating")
    private double rating;
    @JsonProperty("stock")
    private int stock;
    @JsonProperty("brand")
    private String brand;
    @JsonProperty("category")
    private String category;
    @JsonProperty("thumbnail")
    private String thumbnail;

    public int getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public double getRating() {
        return rating;
    }

    public int getStock() {
        return stock;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public String getThumbnail() {
        return thumbnail;
    }

}
