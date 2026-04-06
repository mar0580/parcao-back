package com.parcao.controller;

import com.parcao.model.EmailDetails;
import com.parcao.service.IEmailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailControllerTest {

    private static final String CORRELATION_ID = "test-correlation-id";

    @Mock
    private IEmailService emailService;

    @InjectMocks
    private EmailController emailController;

    @BeforeEach
    void setUp() {
        MDC.put("correlationId", CORRELATION_ID);
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldSendSimpleMailSuccessfully() {
        EmailDetails details = buildEmailDetails();
        String expected = "Mail Sent Successfully";
        when(emailService.sendSimpleMail(details)).thenReturn(expected);

        String response = emailController.sendMail(details);

        assertEquals(expected, response);
        verify(emailService, times(1)).sendSimpleMail(details);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    void shouldSendMailWithAttachmentSuccessfully() {
        EmailDetails details = buildEmailDetails();
        String expected = "Mail with attachment sent successfully";
        when(emailService.sendMailWithAttachment(details)).thenReturn(expected);

        String response = emailController.sendMailWithAttachment(details);

        assertEquals(expected, response);
        verify(emailService, times(1)).sendMailWithAttachment(details);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    void shouldSendSimpleMailEvenWhenCorrelationIdIsMissing() {
        MDC.remove("correlationId");
        EmailDetails details = buildEmailDetails();
        String expected = "Mail queued";
        when(emailService.sendSimpleMail(details)).thenReturn(expected);

        String response = emailController.sendMail(details);

        assertEquals(expected, response);
        verify(emailService, times(1)).sendSimpleMail(details);
        verifyNoMoreInteractions(emailService);
    }

    private EmailDetails buildEmailDetails() {
        return new EmailDetails("Mensagem de teste", "Assunto", "anexo.pdf", "financeiro");
    }
}