package com.example.angularjwtuploadfilebe.service.impl;

import com.example.angularjwtuploadfilebe.model.Role;
import com.example.angularjwtuploadfilebe.model.RoleName;
import com.example.angularjwtuploadfilebe.repository.IRoleRepository;
import com.example.angularjwtuploadfilebe.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    IRoleRepository roleRepository;
    @Override
    public Optional<Role> findByName(RoleName name) {
        return roleRepository.findByName(name);
    }
}
