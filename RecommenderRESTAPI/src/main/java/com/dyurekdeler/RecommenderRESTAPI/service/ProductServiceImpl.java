package com.dyurekdeler.RecommenderRESTAPI.service;

import com.dyurekdeler.RecommenderRESTAPI.models.Product;
import com.dyurekdeler.RecommenderRESTAPI.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Override
    public List<Product> findAllProduct() {
        return (List<Product>)repository.findAll();
    }

    @Override
    public Product findById(String product_id) {
        Optional<Product> result = repository.findById(product_id);
        return result.orElse(null);
    }

    @Override
    public Product insert(Product p) {
        return repository.save(p);
    }

    @Override
    public boolean delete(String product_id) {
        try {
            repository.deleteById(product_id);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Product p) {
        try {
            repository.save(p);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
