package com.sbezgin.passwordskeeper.utils;

import java.util.Random;

/**
 * Created by sbezgin on 15.06.2016.
 */
public class RandomPass {
    private static final char[] chars = new char[]{'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']',
            'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', ';', 'z', 'x', 'c', 'v', 'b', 'n', 'm',
            'Q','W','E','R','T','Y','U','I','O','P','{','}','|','A','S','D','F','G','H','J','K','L',':','Z','X'
            ,'C','V','B','N','M','<','>','?','&','%','$','#','@','!','*','_','-','+','='};
    public static String generatePass() {
        Random ran = new Random();
        ran.nextInt(chars.length - 1);
        char[] charPass = new char[8];
        for (int i = 0; i < 8; i++) {
            charPass[i] = chars[ran.nextInt(chars.length)];
        }
        return new String(charPass);
    }
}
