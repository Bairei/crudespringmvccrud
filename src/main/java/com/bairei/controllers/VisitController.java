package com.bairei.controllers;

import com.bairei.domain.Visit;
import com.bairei.services.RoleService;
import com.bairei.services.UserService;
import com.bairei.services.VisitService;
import com.bairei.validators.VisitValidator;
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

import java.security.Principal;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.logging.Logger;

@Controller
public class VisitController {

    private final static Logger log = Logger.getLogger(VisitController.class.toString());

    @Autowired
    private VisitService visitService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private VisitValidator validator;

    @InitBinder("visit")
    protected void initBinder(WebDataBinder binder){
        binder.setValidator(validator);
    }

    @RequestMapping(value = "/visits",method = RequestMethod.GET)
    public String list(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if(Arrays.toString(auth.getAuthorities().toArray()).contains("ROLE_ADMIN")){
                model.addAttribute("visits",visitService.findAll());
                return "visits";
            }
        model.addAttribute("visits",visitService.findAllByPatient(userService.findUserByEmail(auth.getName())));
        return "visits";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping("/visit/new")
    public String newVisit(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("method","post");
        model.addAttribute("visit", new Visit());
        model.addAttribute("doctors", userService.findUsersByRolesContaining(roleService.getAdminRole()));
        if(Arrays.toString(auth.getAuthorities().toArray()).contains("ROLE_ADMIN")) {
            model.addAttribute("patients", userService.findUsersByRolesContaining(roleService.getUserRole()));
            return "visitform";
        }
        model.addAttribute("patients", userService.findUserByEmail(auth.getName()));
        return "visitform";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping (value = "/visit", method = RequestMethod.POST)
    public String saveVisit(@ModelAttribute("visit") @Validated Visit visit, BindingResult result,
                            Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        validator.validate(visit,result);
        if(result.hasErrors()){
            model.addAttribute("method","post");
            model.addAttribute("doctors", userService.findUsersByRolesContaining(roleService.getAdminRole()));
            if(Arrays.toString(auth.getAuthorities().toArray()).contains("ROLE_ADMIN")) {
                model.addAttribute("patients", userService.findUsersByRolesContaining(roleService.getUserRole()));
                return "visitform";
            }
            model.addAttribute("patients", userService.findUserByEmail(auth.getName()));
            return "visitform";
        }
        visitService.save(visit);
        return "redirect:/visit/" + visit.getId();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping (value = "/visit", method = RequestMethod.PATCH)
    public String updateVisit(@ModelAttribute("visit") @Validated Visit visit, BindingResult result,
                              Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        validator.validate(visit,result);
        if(result.hasErrors()){
            model.addAttribute("method","patch");
            model.addAttribute("doctors", userService.findUsersByRolesContaining(roleService.getAdminRole()));
            if(Arrays.toString(auth.getAuthorities().toArray()).contains("ROLE_ADMIN")) {
                model.addAttribute("patients", userService.findUsersByRolesContaining(roleService.getUserRole()));
                return "visitform";
            }
            model.addAttribute("patients", userService.findUserByEmail(auth.getName()));
            return "visitform";
        }
        visitService.saveOrUpdate(visit);
        return "redirect:/visit/" + visit.getId();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping (value = "/visit/{id}")
    public String showVisit (@PathVariable Integer id, Model model){
        Visit visit = visitService.findOne(id);
        if(visit != null) {
            model.addAttribute("visit", visit);
            return "visitshow";
        }
        return "redirect:/visits/";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping("/visit/edit/{id}")
    public String editVisit(@PathVariable Integer id, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Visit visit = visitService.findOne(id);
        if(visit != null){
            if(!auth.getName().equals(visitService.findOne(id).getPatient().getEmail()) && !auth.getName().equals(visit.getDoctor().getEmail())){
                return "redirect:/visits/";
            }
            model.addAttribute("method","patch");
            model.addAttribute("visit", visit);
            model.addAttribute("doctors", userService.findUsersByRolesContaining(roleService.getAdminRole()));
            if(Arrays.toString(auth.getAuthorities().toArray()).contains("ROLE_ADMIN")) {
                model.addAttribute("patients", userService.findUsersByRolesContaining(roleService.getUserRole()));
                return "visitform";
            }
            model.addAttribute("patients", userService.findOne(visitService.findOne(id).getPatient().getId()));
            return "visitform";
        }
        return "redirect:/visits/";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(value = "/visit/delete/{id}", method = RequestMethod.DELETE)
    public String deleteVisit(@PathVariable Integer id){
        try {
            visitService.delete(id);
            return "redirect:/visits";
        } catch (Exception e) {
            return "redirect:/visits";
        }
    }
}
