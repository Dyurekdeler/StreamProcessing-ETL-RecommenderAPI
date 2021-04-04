package com.dyurekdeler.RecommenderRESTAPI.models;

import java.util.ArrayList;
import java.util.List;

public class ProductViewResponse {

    private String userId;
    private List<String> productIds = new ArrayList<>();
    private String type;

    public ProductViewResponse(){

    }

    public ProductViewResponse(String userId, List<String> productIds, String type) {
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
