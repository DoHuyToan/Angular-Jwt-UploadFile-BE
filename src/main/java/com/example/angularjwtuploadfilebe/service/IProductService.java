package com.example.angularjwtuploadfilebe.service;

import com.example.angularjwtuploadfilebe.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    boolean existsByNameProduct(String nameProduct);
    List<Product> findAll();
    Page<Product> findAll(Pageable pageable);
    void deleteId(Long id);
    Product save(Product product);
    Optional<Product> findById(Long id);
}
