package com.example.angularjwtuploadfilebe.service.impl;

import com.example.angularjwtuploadfilebe.model.Product;
import com.example.angularjwtuploadfilebe.model.User;
import com.example.angularjwtuploadfilebe.repository.IProductRepository;
import com.example.angularjwtuploadfilebe.security.userprincal.UserDetailService;
import com.example.angularjwtuploadfilebe.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    UserDetailService userDetailService;
    @Autowired
    IProductRepository productRepository;

    @Override
    public boolean existsByNameProduct(String nameProduct) {
        return productRepository.existsByNameProduct(nameProduct);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public void deleteId(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product save(Product product) {
        //  HÀM getCurrentUser() ĐỂ LẤY RA USER HIỆN TẠI ĐỂ THỰC HIỆN THAO TÁC VỚI DB
        User user = userDetailService.getCurrentUser();
//        set User vào để biết ai là người thêm sản phẩm vào
        product.setUser(user);
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.empty();
    }
}
