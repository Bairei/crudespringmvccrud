package com.bairei.services;

import com.bairei.domain.Role;
import com.bairei.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Set<Role> createAdminRole() {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleRepository.findByName("ROLE_ADMIN"));
        return roleSet;
    }

    @Override
    public Set<Role> createUserRole() {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleRepository.findByName("ROLE_USER"));
        return roleSet;
    }

    @Override
    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER");
    }

    @Override
    public Role getAdminRole() {
        return roleRepository.findByName("ROLE_ADMIN");
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
