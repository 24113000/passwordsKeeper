package com.sbezgin.passwordskeeper.service;

public interface PasswordHolder {
    String getPassword();

    void setPassword(String password);

    void cleanPassword();


}
