package com.bairei.services;

import com.bairei.domain.User;
import com.bairei.domain.Visit;

import java.util.List;

public interface VisitService {
    void appointmentReminder();
    Visit save(Visit visit);
    List<Visit> findAll();
    Visit findOne(Integer id);
    void delete(Integer id);
    List<Visit> findAllByPatient(User patient);
}
