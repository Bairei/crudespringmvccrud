package com.bairei.crudespringmvccrud.services;

public interface MailingService {
    void sendSimpleMessage(String to, String subject, String text);
}
