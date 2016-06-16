package com.sbezgin.passwordskeeper.service.impl;

import com.sbezgin.passwordskeeper.service.PasswordHolder;

/**
 * Created by sbezgin on 08.06.2016.
 */
public class PasswordHolderImpl implements PasswordHolder {

    private static PasswordHolder instance = new PasswordHolderImpl();
    public static PasswordHolder getInstance() {
        return instance;
    }

    private String pass;
    private PasswordHolderImpl() {}

    @Override
    public String getPassword() {
        return pass;
    }

    @Override
    public void setPassword(String password) {
        this.pass = password;
    }

    @Override
    public void cleanPassword() {
        pass = null;
    }
}
