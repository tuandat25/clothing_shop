package com.tuandat.clothingshop.services.role;

import com.tuandat.clothingshop.models.Role;

import java.util.*;

public interface IRoleService {
    List<Role> getAllRoles();
    String findRoleNameById(UUID roleId);
}
