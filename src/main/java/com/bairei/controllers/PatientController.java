package com.bairei.controllers;

import com.bairei.domain.User;
import com.bairei.services.RoleService;
import com.bairei.services.UserService;
import com.bairei.validators.PatientValidator;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Controller
public class PatientController {

    private final static Logger log = Logger.getLogger(PatientController.class.toString());

    private UserService userService;
    private RoleService roleService;
    private PatientValidator validator;

    @Autowired
    public PatientController(UserService userService, RoleService roleService, PatientValidator validator) {
        this.userService = userService;
        this.roleService = roleService;
        this.validator = validator;
    }

    @InitBinder("patient")
    protected void initBinder(WebDataBinder binder){
        binder.setValidator(validator);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/patients",method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("patients", userService.findUsersByRolesContaining(roleService.getUserRole()));
        return "patients";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/patient/new")
    public String newPatient(Model model){
        model.addAttribute("patient", new User());
        model.addAttribute("method", "post");
        return "patientform";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping (value = "patient", method = RequestMethod.POST)
    public String savePatient(@ModelAttribute(name = "patient") @Validated User patient, BindingResult result, Model model){
        if (result.hasErrors()){
            model.addAttribute("method", "post");
            return "patientform";
        }
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        String randomPass = generator.generate(20);
        patient.setPassword(randomPass);
        patient.setConfirmPassword(randomPass);
        patient.setSecret("");
        patient.setRoles(roleService.createUserRole());
        try {
            log.info(patient.toString() + patient.getPassword());
            userService.save(patient);
        } catch (Exception e) {
            log.warning(e.toString());
            model.addAttribute("method","post");
            return "patientform";
        }
        return "redirect:/patient/" + patient.getId();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping (value = "patient/{id}")
    public String showPatient (@PathVariable Integer id, Model model){
        User patient = userService.findOne(id);
        if (patient != null) {
            model.addAttribute("patient", patient);
            return "patientshow";
        }
        return "redirect:/patients/";

    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "patient/delete/{id}", method = RequestMethod.DELETE)
    public String deletePatient(@PathVariable Integer id){
        try {
            userService.delete(id);
            return "redirect:/patients";
        } catch (Exception e) {
            return "redirect:/patients";
        }
    }
}
