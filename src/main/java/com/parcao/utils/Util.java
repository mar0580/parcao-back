package com.parcao.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public static Timestamp dateToTimestamp(String data) throws ParseException {
        System.out.println("dataInicial: " + data);
        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(data);
        System.out.println("Timestamp: " + new Timestamp(date1.getTime()));
        return new Timestamp(date1.getTime());
    }
}
