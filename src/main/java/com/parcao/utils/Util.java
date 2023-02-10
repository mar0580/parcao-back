package com.parcao.utils;

import com.parcao.models.EmailDetails;
import com.parcao.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
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

    public static Timestamp dateToInicialTimestamp(String data) throws ParseException {
        System.out.println("dataInicial: " + data);
        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(data);
        return new Timestamp(date1.getTime());
    }

    public static Timestamp dateToFinalTimestamp(String data) throws ParseException {
        System.out.println("dataInicial: " + data);
        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(data);
        return new Timestamp(date1.getTime() + TimeUnit.HOURS.toMillis(23) + + TimeUnit.MINUTES.toMillis(59) + + TimeUnit.SECONDS.toMillis(59));
    }
}
