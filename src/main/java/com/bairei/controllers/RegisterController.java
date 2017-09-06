package com.bairei.controllers;

import com.bairei.domain.User;
import com.bairei.services.RoleService;
import com.bairei.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String userForm(Model model){
        model.addAttribute("user", new User());
        return "userform";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute @Validated User user, BindingResult result){
        if (result.hasErrors()){
            return "userform";
        }
        if (user.getSecret().equals("superacc")){
            user.setRoles(roleService.createAdminRole());
        } else user.setRoles(roleService.createUserRole());
        try {
            userService.save(user);
        } catch (Exception e) {
            return "userform";
        }
        return "redirect:/";
    }
}
