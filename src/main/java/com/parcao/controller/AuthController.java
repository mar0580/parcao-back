package com.parcao.controller;

import com.parcao.exception.UserAlreadyExistsException;
import com.parcao.model.dto.LoginRequestDTO;
import com.parcao.model.dto.LoginResponseDTO;
import com.parcao.model.dto.SignupRequestDTO;
import com.parcao.model.enums.MensagemEnum;
import com.parcao.payload.response.MessageResponse;
import com.parcao.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private static final String CORRELATION_ID = "correlationId";

    private final IAuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<LoginResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO request) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Iniciando autenticação para usuário: {}", correlationId, request.getUserName());

        LoginResponseDTO response = authService.authenticate(request);

        LOGGER.info("[correlationId={}] Autenticação concluída com sucesso para usuário: {}", correlationId, request.getUserName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequestDTO signUpRequest) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Iniciando registro de usuário: {}", correlationId, signUpRequest.getUserName());

        try {
            authService.registerUser(signUpRequest);
            LOGGER.info("[correlationId={}] Registro concluído com sucesso para usuário: {}", correlationId, signUpRequest.getUserName());
            return ResponseEntity.ok(MensagemEnum.SUCESSO);
        } catch (UserAlreadyExistsException exception) {
            LOGGER.warn("[correlationId={}] Usuário já existe: {}", correlationId, signUpRequest.getUserName());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(MensagemEnum.USUARIO_JA_EXISTE);
        } catch (RuntimeException exception) {
            LOGGER.error("[correlationId={}] Erro ao registrar usuário: {}", correlationId, exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<Object> logoutUser() {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Iniciando logout", correlationId);

        ResponseCookie cookie = authService.getCleanJwtCookie();

        LOGGER.info("[correlationId={}] Logout concluído", correlationId);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse(MensagemEnum.SUCESSO.getMensagem()));
    }

    private String currentCorrelationId() {
        return MDC.get(CORRELATION_ID);
    }
}
