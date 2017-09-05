package com.bairei.controllers;

import com.bairei.domain.Visit;
import com.bairei.repositories.DoctorRepository;
import com.bairei.repositories.PatientRepository;
import com.bairei.services.MailingService;
import com.bairei.services.VisitService;
import com.bairei.validators.VisitValidator;
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
public class VisitController {

    @Autowired
    private VisitService visitService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private VisitValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder){
        binder.setValidator(validator);
    }

    private static final Logger log = Logger.getLogger(String.valueOf(VisitController.class));

    @RequestMapping(value = "/visits",method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("visits",visitService.findAll());
        return "visits";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping("/visit/new")
    public String newVisit(Model model){
        model.addAttribute("method","post");
        model.addAttribute("visit", new Visit());
        model.addAttribute("doctors", doctorRepository.findAll());
        model.addAttribute("patients", patientRepository.findAll());
        return "visitform";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping (value = "visit", method = RequestMethod.POST)
    public String saveVisit(@ModelAttribute("visit") @Validated Visit visit, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("method","post");
            model.addAttribute("doctors", doctorRepository.findAll());
            model.addAttribute("patients", patientRepository.findAll());
            return "visitform";
        }
        visitService.save(visit);
        return "redirect:/visit/" + visit.getId();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping (value = "visit", method = RequestMethod.PATCH)
    public String updateVisit(@ModelAttribute("visit") @Validated Visit visit, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("method","patch");
            model.addAttribute("doctors", doctorRepository.findAll());
            model.addAttribute("patients", patientRepository.findAll());
            return "visitform";
        }
        visitService.save(visit);
        return "redirect:/visit/" + visit.getId();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping (value = "visit/{id}")
    public String showVisit (@PathVariable Integer id, Model model){
        Visit visit = visitService.findOne(id);
        if(visit != null) {
            model.addAttribute("visit", visit);
            return "visitshow";
        }
        return "redirect:/visits/";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping("visit/edit/{id}")
    public String editVisit(@PathVariable Integer id, Model model){
        Visit visit = visitService.findOne(id);
        if(visit != null){
            model.addAttribute("method","patch");
            model.addAttribute("visit", visit);
            model.addAttribute("doctors", doctorRepository.findAll());
            model.addAttribute("patients", patientRepository.findAll());
            return "visitform";
        }
        return "redirect:/visits/";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(value = "visit/delete/{id}", method = RequestMethod.DELETE)
    public String deleteVisit(@PathVariable Integer id){
        try {
            visitService.delete(id);
            return "redirect:/visits";
        } catch (Exception e) {
            return "redirect:/visits";
        }
    }
}
