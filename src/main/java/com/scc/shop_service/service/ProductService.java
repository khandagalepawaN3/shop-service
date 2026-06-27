package com.scc.shop_service.service;

import com.scc.shop_service.entity.Product;
import com.scc.shop_service.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> getAllProducts(){
        return repository.findAll();
    }

    public Product save(Product product){
        return repository.save(product);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    public Product getById(Long id){

        return repository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public long countProducts(){
        return repository.count();
    }

}
