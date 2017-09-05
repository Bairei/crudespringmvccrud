package com.bairei.bootstrap;

import com.bairei.domain.Doctor;
import com.bairei.domain.Patient;
import com.bairei.repositories.DoctorRepository;
import com.bairei.repositories.PatientRepository;
import com.bairei.services.MailingService;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Component
public class DoctorAndPatientLoader implements SmartInitializingSingleton {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MailingService mailingService;

    private static final Logger log = Logger.getLogger(DoctorAndPatientLoader.class.toString());

    @Override
    public void afterSingletonsInstantiated() {

        Doctor doctor = new Doctor();
        doctor.setName("asdasd");
        doctor.setSurname("orortiehn");
        doctorRepository.save(doctor);
        log.info("Saved doctor - id " + doctor.getId());


        Patient patient = new Patient();
        patient.setName("jan");
        patient.setSurname("kowalski");
        patient.setEmail("jakis@email.com");
        patientRepository.save(patient);
        log.info("Saved patient - id " + patient.getId());

        Doctor doctor1 = new Doctor();
        doctor1.setName("probny");
        doctor1.setSurname("doktor");
        doctorRepository.save(doctor1);
        log.info("Saved doctor - id "+doctor1.getId());

//        log.info("trying to send message:");
//        mailingService.sendSimpleMessage("baireikawagishi@gmail.com","hello world", "this message " +
//                "is sent with spring framework's mailing service :)");

    }
}