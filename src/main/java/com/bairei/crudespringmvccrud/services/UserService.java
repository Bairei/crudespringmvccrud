package com.bairei.crudespringmvccrud.services;

import com.bairei.crudespringmvccrud.domain.Role;
import com.bairei.crudespringmvccrud.domain.User;

import java.util.List;

public interface UserService {
    User findUserByEmail (String email);
    User save(User user) throws Exception;
    List<User> findAll();
    User findOne(Integer id);
    List<User> findUsersByRolesContaining(Role role);
    void delete(Integer id);
}
