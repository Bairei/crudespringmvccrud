package com.bairei.services;

import com.bairei.domain.Role;
import com.bairei.domain.User;

import java.util.List;

public interface UserService {
    User findUserByEmail (String email);
    User save(User user) throws Exception;
    List<User> findAll();
    User findOne(Integer id);
    List<User> findUsersByRolesContaining(Role role);
    void delete(Integer id);
}
