package com.dyurekdeler.RecommenderRESTAPI.service;

import com.dyurekdeler.RecommenderRESTAPI.models.Bestseller;
import org.springframework.stereotype.Component;

@Component
public interface BestsellerService {

    Bestseller getProductRecommendation(String userId);
}
