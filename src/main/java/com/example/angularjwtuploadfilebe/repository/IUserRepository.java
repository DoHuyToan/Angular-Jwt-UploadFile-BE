package com.example.angularjwtuploadfilebe.repository;

import com.example.angularjwtuploadfilebe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // Khi đăng nhập sẽ Tìm kiếm User có tồn tại trong DB ko?
    // Boolean existBy... để ko ko bị trùng lặp
    Boolean existsByUsername(String username); // khi tạo dữ liệu ktra username đã có trong DB chưa
    Boolean existsByEmail(String email);       // khi tạo dữ liệu ktra email đã có trong DB chưa
}
