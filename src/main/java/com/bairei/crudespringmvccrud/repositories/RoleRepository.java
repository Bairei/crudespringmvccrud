package com.bairei.crudespringmvccrud.repositories;

import com.bairei.crudespringmvccrud.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByName(String role);
}
