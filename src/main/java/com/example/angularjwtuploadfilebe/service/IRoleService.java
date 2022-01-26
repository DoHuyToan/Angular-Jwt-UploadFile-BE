package com.example.angularjwtuploadfilebe.service;

import com.example.angularjwtuploadfilebe.model.Role;
import com.example.angularjwtuploadfilebe.model.RoleName;

import java.util.Optional;

public interface IRoleService {
    //    để khi creat dữ liệu truyền vào là PM hay là ADMIN
    Optional<Role> findByName(RoleName name);
}
