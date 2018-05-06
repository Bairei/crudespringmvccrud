package com.bairei.crudespringmvccrud.services;

import com.bairei.crudespringmvccrud.domain.Role;
import com.bairei.crudespringmvccrud.domain.User;
import com.bairei.crudespringmvccrud.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User save(User user) throws Exception {
        if(user.getSecret() == null) user.setSecret("");
        if(user.getPassword() == null || user.getPassword().equals("")
                || user.getEmail().equals("") || user.getEmail() == null || user.getRoles().size() < 1){
            throw new Exception("You can't save an user with no password, email or without any roles!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
        if (!user.getSecret().equals("")){
            user.setSecret(passwordEncoder.encode(user.getSecret()));
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findOne(Integer id) {
        return userRepository.findOne(id);
    }

    @Override
    public List<User> findUsersByRolesContaining(Role role) {
        return userRepository.findUsersByRolesContaining(role);
    }

    @Override
    public void delete(Integer id) {
        userRepository.delete(id);
    }

}
