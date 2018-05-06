package com.bairei.crudespringmvccrud.bootstrap;

import com.bairei.crudespringmvccrud.domain.Role;
import com.bairei.crudespringmvccrud.domain.User;
import com.bairei.crudespringmvccrud.domain.Visit;
import com.bairei.crudespringmvccrud.services.RoleService;
import com.bairei.crudespringmvccrud.services.UserService;
import com.bairei.crudespringmvccrud.services.VisitService;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.logging.Logger;

@Component
public class DoctorAndUserLoader implements SmartInitializingSingleton {

    private UserService userService;
    private RoleService roleService;
    private VisitService visitService;

    @Autowired
    public DoctorAndUserLoader(UserService userService, RoleService roleService,
                               VisitService visitService){
        this.userService = userService;
        this.roleService = roleService;
        this.visitService = visitService;
    }

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

        Visit visit = new Visit();
        visit.setDoctor(doctor);
        visit.setPatient(user);
        visit.setDate(new Date());
        visit.setConsultingRoom("1");
        visitService.save(visit);

    }
}