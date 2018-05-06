package com.bairei.crudespringmvccrud.services;

public interface LoginService {
    void autoLogin(String username, String password);
    String findLoggedInUsername();
}
