package com.bairei.bootstrap;

import com.bairei.domain.Role;
import com.bairei.domain.User;
import com.bairei.repositories.RoleRepository;
import com.bairei.repositories.UserRepository;
import com.bairei.services.RoleService;
import com.bairei.services.UserService;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Component
public class DoctorAndUserLoader implements SmartInitializingSingleton {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private static final Logger log = Logger.getLogger(DoctorAndUserLoader.class.toString());

    @Override
    public void afterSingletonsInstantiated() {

        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        roleService.save(userRole);
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        roleService.save(adminRole);

        User doctor = new User();
        doctor.setName("asdasd");
        doctor.setSurname("orortiehn");
        doctor.setEmail("asd@oro.com");
        doctor.setPassword("asdasd");
        doctor.setConfirmPassword("asdasd");
        doctor.setRoles(roleService.createAdminRole());
        doctor.setSecret("");
        try {
            userService.save(doctor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Saved doctor - id " + doctor.getId());

        User doctor1 = new User();
        doctor1.setName("probny");
        doctor1.setSurname("doktor");
        doctor1.setPassword("probny");
        doctor1.setConfirmPassword("probny");
        doctor1.setEmail("probny@doktor.pl");
        doctor1.setRoles(roleService.createAdminRole());
        doctor1.setSecret("");
        try {
            userService.save(doctor1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Saved doctor - id "+doctor1.getId());

        User user = new User();
        user.setName("Jan");
        user.setSurname("Kowalski");
        user.setEmail("jakismail@mail.pl");
        user.setPassword("pass");
        user.setConfirmPassword("pass");
        user.setSecret("");
        user.setRoles(roleService.createUserRole());
        try {
            userService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Saved user - id " + user.getId());
//        log.info(userService.findAll().toString());

    }
}