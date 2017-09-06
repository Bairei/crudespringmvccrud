package com.bairei.services;

public interface LoginService {
    void autoLogin(String username, String password);
    String findLoggedInUsername();
}
