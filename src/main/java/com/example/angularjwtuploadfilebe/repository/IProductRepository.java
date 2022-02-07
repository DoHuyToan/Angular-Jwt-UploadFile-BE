package com.example.angularjwtuploadfilebe.repository;

import com.example.angularjwtuploadfilebe.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    // khi tạo dữ liệu ktra name sản phẩm đã có trong DB chưa
    boolean existsByName(String name);
}
