package org.softdesign.model.product;

public class ProductRequest {

    private String title;
    private String description;
    private int price;
    private double discountPercentage;
    private double rating;
    private int stock;
    private String brand;
    private String category;
    private String thumbnail;

    public ProductRequest(String title, String description, int price, double discountPercentage, double rating, int stock, String brand, String category, String thumbnail) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.rating = rating;
        this.stock = stock;
        this.brand = brand;
        this.category = category;
        this.thumbnail = thumbnail;
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

    @Override
    public String toString() {
        return "{" +
                "\"title\": \"" + title + "\"," +
                "\"description\": \"" + description + "\"," +
                "\"price\": " + price + "," +
                "\"discountPercentage\": " + discountPercentage + "," +
                "\"rating\": " + rating + "," +
                "\"stock\": " + stock + "," +
                "\"brand\": \"" + brand + "\"," +
                "\"category\": \"" + category + "\"," +
                "\"thumbnail\": \"" + thumbnail + "\"" +
                "}";
    }
}
