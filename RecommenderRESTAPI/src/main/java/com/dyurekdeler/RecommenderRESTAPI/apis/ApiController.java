package com.dyurekdeler.RecommenderRESTAPI.apis;

import com.dyurekdeler.RecommenderRESTAPI.models.Bestseller;
import com.dyurekdeler.RecommenderRESTAPI.models.ProductViewResponse;
import com.dyurekdeler.RecommenderRESTAPI.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController

@Component
@ComponentScan("com.dyurekdeler.RecommenderRESTAPI.service")
public class ApiController {

    @Autowired
    ProductViewHistoryService productViewHistoryService;

    @Autowired
    BestsellerService bestsellerService;

    @GetMapping("/api/products-viewed")
    public ProductViewResponse getLastViewedProducts(@RequestParam(value = "user-id") String id) {
        List<String> productIds = productViewHistoryService.getLastViewedProducts(id);
        return new ProductViewResponse(id, productIds, "personalized");
    }

    @DeleteMapping("/api/delete-product-view-history")
    public boolean deleteProductViewHistory(@RequestParam(value = "user-id") String userId, @RequestParam(value = "product-id") String productId){
        return productViewHistoryService.deleteProductViewHistory(userId, productId);
    }

    @GetMapping("/api/get-product-recommendation")
    public Bestseller getProductRecommendation(@RequestParam(value = "user-id") String id){
        return bestsellerService.getProductRecommendation(id);

    }
}
