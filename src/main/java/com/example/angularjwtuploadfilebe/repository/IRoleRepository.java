package com.example.angularjwtuploadfilebe.repository;

import com.example.angularjwtuploadfilebe.model.Role;
import com.example.angularjwtuploadfilebe.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
//    để khi creat dữ liệu truyền vào là USER, PM hay là ADMIN
    Optional<Role> findByName(RoleName name);
}
