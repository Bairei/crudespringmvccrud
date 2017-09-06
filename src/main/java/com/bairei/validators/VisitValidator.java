package com.bairei.validators;

import com.bairei.domain.Visit;
import com.bairei.services.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class VisitValidator implements Validator {

    @Autowired
    private VisitService visitService;

    @Override
    public boolean supports(Class<?> aClass) {
        return Visit.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Visit visit = (Visit) o;
        ValidationUtils.rejectIfEmpty(errors,"date", "NotEmpty.visitForm.date","You must enter a valid date");
        ValidationUtils.rejectIfEmpty(errors, "doctor", "NotEmpty.visitForm.doctor","You must create a visit with a doctor!");
        ValidationUtils.rejectIfEmpty(errors,"patient", "NotEmpty.visitForm.patient","You can't create a visit with no patient!");
        ValidationUtils.rejectIfEmpty(errors, "consultingRoom", "NotEmpty.visitForm.consultingRoom","You must enter a consulting room!");

        LocalDate futureDate = LocalDate.now().plusYears(5);
        Date date = new Date();

        if (visit.getDate().after(Date.from(futureDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))){
            errors.rejectValue("date","NotFuture.visitForm.date","You must enter date before " + futureDate + "!");
        }
        if (visit.getDate().before(date)){
            errors.rejectValue("date","NotPast.visitForm.date","You must enter date after " + date + "!");
        }

        for (Visit v: visitService.findAll()) {
            if((v.getDoctor().getId().equals(visit.getDoctor().getId()) || v.getPatient().getId().equals(visit.getPatient().getId()) || v.getConsultingRoom().equals(visit.getConsultingRoom())
            ) && (Math.abs(v.getDate().getTime()-visit.getDate().getTime())<1000*60*30) && !(v.getId().equals(visit.getId()))){
                errors.rejectValue("date", "NotInterfere.visitForm.date","Your date cannot interfere within 30 minutes from an another one! (Either doctor's, patient's or this room's)");
            }
            if(v.getDoctor().getId().equals(visit.getDoctor().getId())
                    && v.getPatient().getId().equals(visit.getPatient().getId())
                    && v.getDate().equals(visit.getDate()) && !v.getId().equals(visit.getId())){
                errors.rejectValue("date","Unique.visitForm.date","You cannot create a new visit with the same patient and doctor on the same date" +
                        "as the other one in database!");
            }
        }

        Pattern p = Pattern.compile("^[1-9]$|^1[0-9]$|^20$");
        Matcher m = p.matcher(visit.getConsultingRoom());
        if(!m.matches()){
            errors.rejectValue("consultingRoom","Valid.visitForm.consultingRoom", "You must enter a correct consulting room number! (1-20)");
        }
    }
}
