package com.example.angularjwtuploadfilebe.service;

import com.example.angularjwtuploadfilebe.model.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> findByUsername(String name); // Khi đăng nhập sẽ Tìm kiếm User có tồn tại trong DB ko?
    // Boolean existBy... để ko ko bị trùng lặp
    Boolean existsByUsername(String username); // khi tạo dữ liệu ktra username đã có trong DB chưa
    Boolean existsByEmail(String email);       // khi tạo dữ liệu ktra email đã có trong DB chưa
//    sau khi tạo mới xong Save dữ liệu vào DB
    User save(User user);
}
