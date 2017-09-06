package com.bairei.validators;

import com.bairei.domain.User;
import com.bairei.repositories.RoleRepository;
import com.bairei.repositories.UserRepository;
import com.bairei.services.RoleService;
import com.bairei.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class DoctorValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User doctor = (User) o;
        ValidationUtils.rejectIfEmpty(errors, "name","NotEmpty.doctorForm.name","Please enter a name!");
        ValidationUtils.rejectIfEmpty(errors,"surname", "NotEmpty.doctorForm.surname", "Please enter a surname!");
        ValidationUtils.rejectIfEmpty(errors,"email", "NotEmpty.doctorForm.email", "Please enter an email!");
        ValidationUtils.rejectIfEmpty(errors,"password", "NotEmpty.doctorForm.password", "Please enter a password!");
        ValidationUtils.rejectIfEmpty(errors,"confirmPassword", "NotEmpty.doctorForm.confirmPassword", "Please confirm a password!");


        for (User d: userService.findAll()){
            if(d.getEmail().equals(doctor.getEmail()) && !d.getId().equals(doctor.getId())){
                errors.rejectValue("surname", "Unique.doctorForm.email", "There is already a doctor with entered email!");
            }
        }

        if(!doctor.getPassword().equals(doctor.getConfirmPassword())){
            errors.rejectValue("password", "Different.doctorForm.password", "Entered passwords are not the same!");
        }
    }
}
