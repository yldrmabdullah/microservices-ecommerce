package com.valven.ecommerce.ui.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Product {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String category;
    private String imageUrl;

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isInStock() {
        return stock != null && stock > 0;
    }

    public boolean hasEnoughStock(int requestedQuantity) {
        return stock != null && stock >= requestedQuantity;
    }

    public String getStockStatus() {
        if (stock == null || stock == 0) {
            return "Out of Stock";
        } else if (stock <= 5) {
            return "Low Stock (" + stock + " left)";
        } else {
            return "In Stock (" + stock + " available)";
        }
    }

    public String getStockStatusClass() {
        if (stock == null || stock == 0) {
            return "out-of-stock";
        } else if (stock <= 5) {
            return "low-stock";
        } else {
            return "in-stock";
        }
    }
}
