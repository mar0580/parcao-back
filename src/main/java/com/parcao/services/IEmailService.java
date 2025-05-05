package com.parcao.services;

import com.parcao.model.entity.EmailDetails;

public interface IEmailService {

    String sendSimpleMail(EmailDetails details);

    String sendMailWithAttachment(EmailDetails details);

    void sendEmail(String recipient, String msgBody, String levelCode);
}
