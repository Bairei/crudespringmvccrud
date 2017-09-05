package com.bairei.controllers;

import com.bairei.domain.Patient;
import com.bairei.repositories.PatientRepository;
import com.bairei.validators.PatientValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
public class PatientController {

    private PatientRepository patientRepository;

    @Autowired
    public void setPatientRepository(PatientRepository patientRepository) { this.patientRepository = patientRepository; }

    @Autowired
    private PatientValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder){
        binder.setValidator(validator);
    }

    @RequestMapping(value = "/patients",method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("patients", patientRepository.findAll());
        return "patients";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping("/patient/new")
    public String newPatient(Model model){
        model.addAttribute("patient", new Patient());
        model.addAttribute("method", "post");
        return "patientform";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping (value = "patient", method = RequestMethod.POST)
    public String savePatient(@ModelAttribute @Validated Patient patient, BindingResult result, Model model){
        if (result.hasErrors()){
            model.addAttribute("method", "post");
            return "patientform";
        }
        patientRepository.save(patient);
        return "redirect:/patient/" + patient.getId();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping (value = "patient", method = RequestMethod.PATCH)
    public String updatePatient(@ModelAttribute @Validated Patient patient, BindingResult result, Model model){
        if (result.hasErrors()){
            model.addAttribute("method", "patch");
            return "patientform";
        }
        patientRepository.save(patient);
        return "redirect:/patient/" + patient.getId();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping (value = "patient/{id}")
    public String showPatient (@PathVariable Integer id, Model model){
        Patient patient = patientRepository.findOne(id);
        if (patient != null) {
            model.addAttribute("patient", patient);
            return "patientshow";
        }
        return "redirect:/patients/";

    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping("patient/edit/{id}")
    public String editPatient(@PathVariable Integer id, Model model){
        Patient patient = patientRepository.findOne(id);
        if (patient != null) {
            model.addAttribute("patient", patient);
            model.addAttribute("method","patch");
            return "patientform";
        }
        return "redirect:/patients/";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(value = "patient/delete/{id}", method = RequestMethod.DELETE)
    public String deletePatient(@PathVariable Integer id){
        try {
            patientRepository.delete(id);
            return "redirect:/patients";
        } catch (Exception e) {
            return "redirect:/patients";
        }
    }

}
