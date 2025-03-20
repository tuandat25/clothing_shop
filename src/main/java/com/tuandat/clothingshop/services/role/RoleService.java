package com.tuandat.clothingshop.services.role;

import com.tuandat.clothingshop.models.Role;
import com.tuandat.clothingshop.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public String findRoleNameById(UUID roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found!"));
        String roleName = role.getName();
        return roleName;
    }
}
