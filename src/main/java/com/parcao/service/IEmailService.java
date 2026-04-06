package com.parcao.service;

import com.parcao.model.EmailDetails;

public interface IEmailService {

    String sendSimpleMail(EmailDetails details);

    String sendMailWithAttachment(EmailDetails details);

    void sendEmail(String recipient, String msgBody, String levelCode);
}
