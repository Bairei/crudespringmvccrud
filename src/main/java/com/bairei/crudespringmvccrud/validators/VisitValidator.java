package com.bairei.crudespringmvccrud.validators;

import com.bairei.crudespringmvccrud.domain.Visit;
import com.bairei.crudespringmvccrud.services.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class VisitValidator implements Validator {

    private final static Logger log = Logger.getLogger(VisitValidator.class.getName());

    private VisitService visitService;

    @Autowired
    public VisitValidator(VisitService visitService) {
        this.visitService = visitService;
    }

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
                log.warning("???");
                errors.rejectValue("date", "NotInterfere.visitForm.date","Your date cannot interfere within 30 minutes from an another one! (Either doctor's, patient's or this room's)");
                break;
            }
        }

        if(visit.getDoctor().getId().equals(visit.getPatient().getId())){
            errors.rejectValue("doctor","notDuplicating.visitForm.doctor", "You cannot create a visit where patient and doctor is the same person!");
        }

        Pattern p = Pattern.compile("^[1-9]$|^1[0-9]$|^20$");
        Matcher m = p.matcher(visit.getConsultingRoom());
        if(!m.matches()){
            errors.rejectValue("consultingRoom","Valid.visitForm.consultingRoom", "You must enter a correct consulting room number! (1-20)");
        }
    }
}
