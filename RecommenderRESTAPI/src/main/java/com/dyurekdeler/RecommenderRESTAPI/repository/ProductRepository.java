package com.dyurekdeler.RecommenderRESTAPI.repository;

import com.dyurekdeler.RecommenderRESTAPI.models.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository  extends CrudRepository<Product, String> {
}
