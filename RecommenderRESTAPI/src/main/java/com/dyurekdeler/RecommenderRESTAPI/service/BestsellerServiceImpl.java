package com.dyurekdeler.RecommenderRESTAPI.service;

import com.dyurekdeler.RecommenderRESTAPI.models.Bestseller;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class BestsellerServiceImpl implements BestsellerService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Bestseller getProductRecommendation(String userId) {
        Query specificCategoryQuery = entityManager.createNativeQuery("SELECT product_id FROM bestseller_product WHERE category_id IN (SELECT category_id FROM product_view_history AS " +
                "PVH INNER JOIN products AS P ON PVH.product_id = P.product_id WHERE user_id = ? LIMIT 3)");
        specificCategoryQuery.setParameter(1,userId);
        @SuppressWarnings("unchecked")
        List<Object> specificBestSellers = specificCategoryQuery.getResultList();
        Bestseller bestSeller = new Bestseller();
        bestSeller.setUserId(userId);

        if(!specificBestSellers.isEmpty()){
            if(specificBestSellers.size() >= 5) {
                for (Object o : specificBestSellers) {
                    bestSeller.getProductIds().add(o.toString());
                }
            }
            bestSeller.settype("personalized");
        } else{
            Query generalCategoryQuery = entityManager.createNativeQuery("SELECT product_id FROM bestseller_product ORDER BY sale_amount DESC LIMIT 10");
            @SuppressWarnings("unchecked")
            List<Object> generalBestSellers = generalCategoryQuery.getResultList();
            bestSeller.settype("non-personalized");
            for(Object o : generalBestSellers ){
                bestSeller.getProductIds().add(o.toString());
            }
        }

        return bestSeller;
    }
}
