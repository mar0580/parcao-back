package com.parcao.controller;

import com.parcao.model.entity.EmailDetails;
import com.parcao.service.IEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/email")
public class EmailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);
    private static final String CORRELATION_ID = "correlationId";

    private final IEmailService emailService;

    public EmailController(IEmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendMail")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public String sendMail(@RequestBody EmailDetails details) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para enviar e-mail simples", correlationId);

        String status = emailService.sendSimpleMail(details);

        LOGGER.info("[correlationId={}] E-mail simples processado com status: {}", correlationId, status);
        return status;
    }

    @PostMapping("/sendMailWithAttachment")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public String sendMailWithAttachment(@RequestBody EmailDetails details) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para enviar e-mail com anexo", correlationId);

        String status = emailService.sendMailWithAttachment(details);

        LOGGER.info("[correlationId={}] E-mail com anexo processado com status: {}", correlationId, status);
        return status;
    }

    private String currentCorrelationId() {
        return MDC.get(CORRELATION_ID);
    }
}
