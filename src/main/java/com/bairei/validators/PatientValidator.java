package com.bairei.validators;

import com.bairei.domain.Patient;
import com.bairei.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PatientValidator implements Validator {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return Patient.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Patient patient = (Patient) o;
        ValidationUtils.rejectIfEmpty(errors,"email","NotEmpty.patientForm.email","Please enter an email!");
        ValidationUtils.rejectIfEmpty(errors,"name","NotEmpty.patientForm.name","Please enter a name!");
        ValidationUtils.rejectIfEmpty(errors,"surname","NotEmpty.patientForm.surname","Please enter a surname!");

        for (Patient p : patientRepository.findAll()){
            if(p.getEmail().equalsIgnoreCase(patient.getEmail()) && !(p.getId().equals(patient.getId()))){
                errors.rejectValue("email","Unique.patientForm.email", "This email is already registered!");
            }
        }
    }
}
