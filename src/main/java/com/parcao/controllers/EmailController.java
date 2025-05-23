package com.parcao.controllers;

import com.parcao.model.entity.EmailDetails;
import com.parcao.services.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    private IEmailService emailService;

    public EmailController(IEmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendMail")
    public String sendMail(@RequestBody EmailDetails details){
        String status = emailService.sendSimpleMail(details);
        return status;
    }

    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(@RequestBody EmailDetails details){
        String status = emailService.sendMailWithAttachment(details);
        return status;
    }
}
