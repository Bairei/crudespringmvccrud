package com.bairei.crudespringmvccrud.validators;

import com.bairei.crudespringmvccrud.domain.User;
import com.bairei.crudespringmvccrud.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PatientValidator implements Validator {

    private UserService userService;

    @Autowired
    public PatientValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        ValidationUtils.rejectIfEmpty(errors,"email","NotEmpty.patientForm.email","Please enter an email!");
        ValidationUtils.rejectIfEmpty(errors,"name","NotEmpty.patientForm.name","Please enter a name!");
        ValidationUtils.rejectIfEmpty(errors,"surname","NotEmpty.patientForm.surname","Please enter a surname!");

        for (User p : userService.findAll()){
            if(p.getEmail().equalsIgnoreCase(user.getEmail()) && !(p.getId().equals(user.getId()))){
                errors.rejectValue("email","Unique.patientForm.email", "This email is already registered!");
            }
        }
    }
}
