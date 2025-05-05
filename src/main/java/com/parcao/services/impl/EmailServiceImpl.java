package com.parcao.services.impl;

import com.parcao.model.entity.EmailDetails;
import java.io.File;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.parcao.services.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${spring.mail.recipient.dev}")
    private String devRecipient;

    @Value("${spring.mail.recipient.user}")
    private String userRecipient;

    private static final String DEV_ERROR = "devError";
    private static final String DEV_WARN = "devWarn";
    private static final String USER_INFO = "userInfo";
    private static final String USER_WARN = "userWarn";

    public String sendSimpleMail(EmailDetails details) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);

            if(details.getTypeRecipient().equals(DEV_ERROR)){
                mailMessage.setTo(devRecipient);
                mailMessage.setSubject("ERRO: " + details.getSubject());
            }

            if(details.getTypeRecipient().equals(DEV_WARN)){
                mailMessage.setTo(devRecipient);
                mailMessage.setSubject("WARN: " + details.getSubject());
            }

            if(details.getTypeRecipient().equals(USER_INFO)){
                mailMessage.setTo(userRecipient);
                mailMessage.setCc(devRecipient);
                mailMessage.setSubject("INFO: " + details.getSubject());
            }
            if(details.getTypeRecipient().equals(USER_WARN)) {
                mailMessage.setTo(userRecipient);
                mailMessage.setCc(devRecipient);
                mailMessage.setSubject("ATENÇÃO: " + details.getSubject());
            }

            mailMessage.setText(details.getMsgBody());
            javaMailSender.send(mailMessage);
            return "E-MAIL ENVIADO COM SUCESSO";
        }catch (Exception e) {
            return "ERRO AO ENVIAR E-MAIL";
        }
    }
    public String sendMailWithAttachment(EmailDetails details) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getTypeRecipient().equals(DEV_ERROR) || details.getTypeRecipient().equals(DEV_WARN) ? devRecipient : userRecipient);
            mimeMessageHelper.setCc(devRecipient);
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(details.getSubject());

            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));

            mimeMessageHelper.addAttachment(file.getFilename(), file);
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        } catch (MessagingException e) {
            return "Error while sending mail!!!";
        }
    }
    public void sendEmail(String recipient, String msgBody, String levelCode){
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setTypeRecipient(recipient);
        emailDetails.setMsgBody(msgBody);
        emailDetails.setSubject(levelCode);
        Thread tSendEmail= new Thread(this.sendSimpleMail(emailDetails));
        tSendEmail.start();
    }
}
