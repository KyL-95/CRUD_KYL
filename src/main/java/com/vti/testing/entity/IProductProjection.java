package com.vti.testing.entity;

public interface IProductProjection {
    int getProductId();
    String getProductName();
    int getProductPrice();
    CategoryProjection getCategory();
    interface CategoryProjection{
        String getCategoryName();
    }
}
