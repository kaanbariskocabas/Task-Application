package com.taskapplication.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {

    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

}
