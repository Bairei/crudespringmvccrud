package com.bairei.crudespringmvccrud.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm", iso = DateTimeFormat.ISO.NONE)
    private Date date;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User doctor;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User patient;
    @Pattern(regexp = "^[1-9]$|^1[0-9]$|^20$", message = "You must enter valid room number in range of 1-20")
    private String consultingRoom;

    public Visit(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getConsultingRoom() {
        return consultingRoom;
    }

    public void setConsultingRoom(String consultingRoom) {
        this.consultingRoom = consultingRoom;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }
}
