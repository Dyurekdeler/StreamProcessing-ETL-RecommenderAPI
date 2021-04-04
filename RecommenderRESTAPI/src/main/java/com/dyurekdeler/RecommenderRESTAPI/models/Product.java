package com.dyurekdeler.RecommenderRESTAPI.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "product_id")
    private String productId;

    @Column(name = "category_id")
    private String categoryId;

    public Product(){
    }

    public Product(String productId, String categoryId){
        this.productId = productId;
        this.categoryId = categoryId;
    }

    public String getproductId() {
        return productId;
    }

    public void setproductId(String productId) {
        this.productId = productId;
    }

    public String getcategoryId() {
        return categoryId;
    }

    public void setcategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
