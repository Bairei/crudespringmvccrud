package com.bairei.validators;

import com.bairei.domain.Doctor;
import com.bairei.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class DoctorValidator implements Validator {

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return Doctor.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Doctor doctor = (Doctor) o;
        ValidationUtils.rejectIfEmpty(errors, "name","NotEmpty.doctorForm.name","Please enter a name!");
        ValidationUtils.rejectIfEmpty(errors,"surname", "NotEmpty.doctorForm.surname", "Please enter a surname!");

        for (Doctor d: doctorRepository.findAll()){
            if(d.getName().equals(doctor.getName()) && d.getSurname().equals(doctor.getSurname()) && !(d.getId().equals(doctor.getId()))){
                errors.rejectValue("surname", "Unique.doctorform.name", "There is already a doctor with entered name and surname!");
            }
        }
    }
}
