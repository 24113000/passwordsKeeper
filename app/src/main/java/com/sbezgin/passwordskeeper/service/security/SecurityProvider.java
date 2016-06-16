package com.sbezgin.passwordskeeper.service.security;

public interface SecurityProvider {
    byte[] encryptData(byte[] data, String password) throws Exception;
    byte[] decryptData(byte[] data, String password) throws Exception;
}
