package com.dyurekdeler.RecommenderRESTAPI.service;

import com.dyurekdeler.RecommenderRESTAPI.models.ProductViewHistory;
import com.dyurekdeler.RecommenderRESTAPI.repository.ProductViewHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductViewHistoryServiceImpl implements ProductViewHistoryService {

    @Autowired
    ProductViewHistoryRepository repository;

    @Override
    public List<ProductViewHistory> findAllProductViewHistories() {
        return (List<ProductViewHistory>)repository.findAll();
    }

    @Override
    public ProductViewHistory findById(String messageId) {
        Optional<ProductViewHistory> result = repository.findById(messageId);
        return result.orElse(null);
    }

    @Override
    public ProductViewHistory insert(ProductViewHistory p) {
        return repository.save(p);
    }

    @Override
    public boolean delete(String messageId) {
        try {
            repository.deleteById(messageId);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(ProductViewHistory p) {
        try {
            repository.save(p);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<String> getLastViewedProducts(String userId) {
        return repository.getLastViewedProducts(userId);
    }

    @Override
    public boolean deleteProductViewHistory(String userId, String productId) {
        try {
            repository.deleteProductViewHistory(userId, productId);
            return true;
        } catch (Exception e) {
            System.out.println("DB ERROR: "+ e.getMessage());
            return false;
        }
    }
}
