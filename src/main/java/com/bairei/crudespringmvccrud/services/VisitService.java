package com.bairei.crudespringmvccrud.services;

import com.bairei.crudespringmvccrud.domain.User;
import com.bairei.crudespringmvccrud.domain.Visit;

import java.util.List;

public interface VisitService {
    void appointmentReminder();
    Visit save(Visit visit);
    Visit saveOrUpdate(Visit visit);
    List<Visit> findAll();
    Visit findOne(Integer id);
    void delete(Integer id);
    List<Visit> findAllByPatient(User patient);
}
