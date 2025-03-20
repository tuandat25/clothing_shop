package com.tuandat.clothingshop.repositories;

import com.tuandat.clothingshop.models.Role;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    String findRoleNameById(UUID roleId);
}
