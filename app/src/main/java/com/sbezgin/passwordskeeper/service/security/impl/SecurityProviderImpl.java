package com.sbezgin.passwordskeeper.service.security.impl;

import com.sbezgin.passwordskeeper.service.security.SecurityProvider;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.GeneralSecurityException;

public class SecurityProviderImpl implements SecurityProvider {

    private static final byte[] SALT ={
            (byte) 0x03, (byte) 0x78, (byte) 0x15, (byte) 0xc17,
            (byte) 0x51, (byte) 0xd5, (byte) 0x55, (byte) 0x37
    };

    private Cipher makeCipher(String pass, Boolean decryptMode) throws GeneralSecurityException {

        //Use a KeyFactory to derive the corresponding key from the passphrase:
        PBEKeySpec keySpec = new PBEKeySpec(pass.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(keySpec);

        //Create parameters from the salt and an arbitrary number of iterations:
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(SALT, 42);

        //Set up the cipher:
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");

        //Set the cipher mode to decryption or encryption:
        if(decryptMode){
            cipher.init(Cipher.ENCRYPT_MODE, key, pbeParamSpec);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, key, pbeParamSpec);
        }
        return cipher;
    }

    @Override
    public byte[] encryptData(byte[] data, String password) throws Exception {
        Cipher cipher = makeCipher(password, true);
        return cipher.doFinal(data);
    }

    @Override
    public byte[] decryptData(byte[] data, String password) throws Exception {
        Cipher cipher = makeCipher(password, false);
        return  cipher.doFinal(data);
    }
}
