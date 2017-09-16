package com.bairei.controllers;

import com.bairei.domain.Role;
import com.bairei.domain.User;
import com.bairei.repositories.RoleRepository;
import com.bairei.repositories.UserRepository;
import com.bairei.services.RoleService;
import com.bairei.services.UserService;
import com.bairei.validators.DoctorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Controller
public class DoctorController {

    private final static Logger log = Logger.getLogger(DoctorController.class.toString());

    private UserService userService;
    private RoleService roleService;
    private DoctorValidator validator;

    @Autowired
    public DoctorController(UserService userService, RoleService roleService, DoctorValidator validator) {
        this.userService = userService;
        this.roleService = roleService;
        this.validator = validator;
    }

    @InitBinder ("doctor")
    protected void InitBinder(WebDataBinder binder) { binder.setValidator(validator); }

    @RequestMapping(value = "/doctors",method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("doctors", userService.findUsersByRolesContaining(roleService.getAdminRole()));
        return "doctors";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/doctor/new")
    public String newDoctor(Model model){
        model.addAttribute("doctor", new User());
        model.addAttribute("method", "post");
        return "doctorform";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping (value = "/doctor", method = RequestMethod.POST)
    public String saveDoctor(@ModelAttribute(name = "doctor") @Validated User doctor,
                             BindingResult result, Model model){
        validator.validate(doctor,result);
        if(result.hasErrors()){
            model.addAttribute("method", "post");
            return "doctorform";
        }
        doctor.setRoles(roleService.createUserRole());
        try {
            userService.save(doctor);
        } catch (Exception e) {
            log.warning(e.toString());
            model.addAttribute("method","post");
            return "doctorform";
        }
        return "redirect:/doctor/" + doctor.getId();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping (value = "/doctor", method = RequestMethod.PUT)
    public String updateDoctor(@ModelAttribute(name = "doctor") @Validated User doctor,
                               BindingResult result, Model model){
        validator.validate(doctor,result);
        if(result.hasErrors()){
            model.addAttribute("method","put");
            return "doctorform";
        }
        doctor.setRoles(roleService.createAdminRole());
        try {
            userService.save(doctor);
        } catch (Exception e) {
            log.warning(e.toString());
            model.addAttribute("method","put");
            return "doctorform";
        }
        return "redirect:/doctor/" + doctor.getId();
    }

    @RequestMapping (value = "/doctor/{id}")
    public String showDoctor (@PathVariable Integer id, Model model){
        User doctor = userService.findOne(id);
        if (doctor != null) {
            model.addAttribute("doctor", doctor);
            return "doctorshow";
        }
        return "redirect:/doctors/";

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("doctor/edit/{id}")
    public String editDoctor(@PathVariable Integer id, Model model){
        User doctor = userService.findOne(id);
        if (doctor != null) {
            model.addAttribute("doctor", doctor);
            model.addAttribute("method","put");
            return "doctorform";
        }
        return "redirect:/doctors/";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "doctor/delete/{id}", method = RequestMethod.DELETE)
    public String deleteDoctor(@PathVariable Integer id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(userService.findOne(id).getEmail().equals(auth.getName())) return "redirect:/doctors";
        try {
            userService.delete(id);
            return "redirect:/doctors";
        } catch (Exception e) {
            log.warning(e.toString());
            return "redirect:/doctors";
        }
    }
}