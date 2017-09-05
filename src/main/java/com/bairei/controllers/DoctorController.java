package com.bairei.controllers;

import com.bairei.domain.Doctor;
import com.bairei.repositories.DoctorRepository;
import com.bairei.validators.DoctorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
public class DoctorController {

    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorValidator validator;

    @Autowired
    public void setDoctorRepository(DoctorRepository doctorRepository){ this.doctorRepository = doctorRepository; }

    @InitBinder
    protected void InitBinder(WebDataBinder binder) { binder.setValidator(validator); }

    @RequestMapping(value = "/doctors",method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("doctors", doctorRepository.findAll());
        return "doctors";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping("/doctor/new")
    public String newDoctor(Model model){
        model.addAttribute("doctor", new Doctor());
        model.addAttribute("method", "post");
        return "doctorform";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping (value = "doctor", method = RequestMethod.POST)
    public String saveDoctor(@ModelAttribute @Validated Doctor doctor, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("method", "post");
            return "doctorform";
        }
        doctorRepository.save(doctor);
        return "redirect:/doctor/" + doctor.getId();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping (value = "doctor", method = RequestMethod.PATCH)
    public String updateDoctor(@ModelAttribute @Validated Doctor doctor, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("method","patch");
            return "doctorform";
        }
        doctorRepository.save(doctor);
        return "redirect:/doctor/" + doctor.getId();
    }

    @RequestMapping (value = "doctor/{id}")
    public String showDoctor (@PathVariable Integer id, Model model){
        Doctor doctor = doctorRepository.findOne(id);
        if (doctor != null) {
            model.addAttribute("doctor", doctor);
            return "doctorshow";
        }
        return "redirect:/doctors/";

    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping("doctor/edit/{id}")
    public String editDoctor(@PathVariable Integer id, Model model){
        Doctor doctor = doctorRepository.findOne(id);
        if (doctor != null) {
            model.addAttribute("doctor", doctor);
            model.addAttribute("method","patch");
            return "doctorform";
        }
        return "redirect:/doctors/";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(value = "doctor/delete/{id}", method = RequestMethod.DELETE)
    public String deleteDoctor(@PathVariable Integer id){
        try {
            doctorRepository.delete(id);
            return "redirect:/doctors";
        } catch (Exception e) {
            return "redirect:/doctors";
        }
    }

}
