package com.sbezgin.service.security;

public interface SecurityProvider {
    byte[] encryptData(byte[] data) throws Exception;
    byte[] decryptData(byte[] data) throws Exception;
}
