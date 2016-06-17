package com.sbezgin.passwordskeeper.utils;

import com.sbezgin.passwordskeeper.service.properties.PropertiesDataHolder;

/**
 * Created by sbezgin on 16.06.2016.
 */
public class Utils {
    public static final String SUPER_HIDDEN_GROUP = "Super hidden";
    public static String getRealGroupName(String group) {
        if (group.equals(SUPER_HIDDEN_GROUP)) {
            return PropertiesDataHolder.SECRET_PROPERTIES;
        }
        return group;
    }

}
