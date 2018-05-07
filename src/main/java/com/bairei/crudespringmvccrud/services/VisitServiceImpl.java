package com.bairei.crudespringmvccrud.services;

import com.bairei.crudespringmvccrud.domain.User;
import com.bairei.crudespringmvccrud.domain.Visit;
import com.bairei.crudespringmvccrud.repositories.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Component
public class VisitServiceImpl implements VisitService {

    private VisitRepository visitRepository;
    private MailingService mailingService;

    @Autowired
    public VisitServiceImpl(VisitRepository visitRepository, MailingService mailingService) {
        this.visitRepository = visitRepository;
        this.mailingService = mailingService;
    }

    private final static Logger log = Logger.getLogger(VisitServiceImpl.class.toString());

    @Override
    @Async
    @Scheduled(cron = "0 0 20 * * *")
    public void appointmentReminder() {
        log.info("Initiating scheduled appointment reminding service");
        for (Visit visit: visitRepository.findAll()){
            Long date = new Date().getTime();
            Long timeDifference = (visit.getDate().getTime()) - date;
            log.info(timeDifference.toString());
            if (timeDifference <= 24*3600*1000 && timeDifference > 0
                    && (visit.getPatient().getEmail() != null
                    && !visit.getPatient().getEmail().equals(""))) {
                mailingService.sendSimpleMessage("baireikawagishi@gmail.com",
                        "Reminder about your visit on " + visit.getDate() + " with doctor "
                                + visit.getDoctor().getName() + " " + visit.getDoctor().getSurname(),
                        "This email has been sent in order to remind you about your appointment with "
                                + visit.getDoctor().getName() + " " + visit.getDoctor().getSurname()
                                + " on " + visit.getDate() + ", in room no. " + visit.getConsultingRoom()
                                + ".\n\n\nThis email has been sent automatically, please DO NOT respond.");
            }
        }
    }

    @Override
    public Visit save(Visit visit) {
        Visit result = visitRepository.save(visit);
        if (result != null
                && result.getPatient().getEmail() != null
                && !result.getPatient().getEmail().equals("")) {
            mailingService.sendSimpleMessage("baireikawagishi@gmail.com", "new visit scheduled on "
                            + visit.getDate(), "You have been successfully scheduled on meeting with doctor "
                            + visit.getDoctor().getName() + " " + visit.getDoctor().getSurname() + " on " + visit.getDate() + ", in room no. " + visit.getConsultingRoom());
        }
        return result;
    }

    @Override
    public Visit saveOrUpdate(Visit visit) {
        return visitRepository.save(visit);
    }

    @Override
    public List<Visit> findAll() {
        return visitRepository.findAll();
    }

    @Override
    public Visit findOne(Integer id) {
        return visitRepository.findById(id).get();
    }

    @Override
    public void delete(Integer id) {
        visitRepository.deleteById(id);
    }

    @Override
    public List<Visit> findAllByPatient(User patient) {
        return visitRepository.findAllByPatient(patient);
    }
}
