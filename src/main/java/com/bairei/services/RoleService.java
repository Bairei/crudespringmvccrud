package com.bairei.services;

import com.bairei.domain.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> createAdminRole();
    Set<Role> createUserRole();
    Role getUserRole();
    Role getAdminRole();
    Role save(Role role);
}
