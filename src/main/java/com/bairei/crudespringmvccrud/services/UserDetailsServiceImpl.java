package com.bairei.crudespringmvccrud.services;

import com.bairei.crudespringmvccrud.domain.Role;
import com.bairei.crudespringmvccrud.domain.User;
import com.bairei.crudespringmvccrud.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final static Logger log = Logger.getLogger(UserDetailsServiceImpl.class.toString());

    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username);

        if(user == null){
            log.warning("User with mail " + username + " was not found! ");
            throw new UsernameNotFoundException("User with email " + username + "was not found!");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                                                user.getPassword(), grantedAuthorities);
    }
}
