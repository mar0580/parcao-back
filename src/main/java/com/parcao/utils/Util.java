package com.parcao.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    private static String REGEX_EMAIL = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";

    public boolean isEmailValid(String email){
        Pattern pattern = Pattern.compile(REGEX_EMAIL);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
}
