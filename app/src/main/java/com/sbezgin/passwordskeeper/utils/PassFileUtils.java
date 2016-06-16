package com.sbezgin.passwordskeeper.utils;


import android.os.Environment;

public abstract class PassFileUtils {
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
