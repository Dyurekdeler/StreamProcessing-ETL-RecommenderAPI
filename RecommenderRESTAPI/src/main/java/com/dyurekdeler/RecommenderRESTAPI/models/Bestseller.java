package com.dyurekdeler.RecommenderRESTAPI.models;

import java.util.ArrayList;
import java.util.List;

public class Bestseller {

    private String userId;
    private ArrayList<String> productIds = new ArrayList<>();
    private String type;

    public Bestseller(){

    }

    public Bestseller(String userId, ArrayList<String> productIds, String type) {
        this.userId = userId;
        this.productIds = productIds;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(ArrayList<String> productIds) {
        this.productIds = productIds;
    }

    public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }
}
