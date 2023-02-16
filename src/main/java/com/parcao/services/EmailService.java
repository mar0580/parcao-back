package com.parcao.services;

import com.parcao.models.EmailDetails;

public interface EmailService {

    String sendSimpleMail(EmailDetails details);

    String sendMailWithAttachment(EmailDetails details);

    void sendEmail(String recipient, String msgBody, String levelCode);
}
