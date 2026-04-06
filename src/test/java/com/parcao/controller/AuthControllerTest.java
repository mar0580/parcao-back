package com.parcao.controller;

import com.parcao.exception.UserAlreadyExistsException;
import com.parcao.model.dto.LoginRequestDTO;
import com.parcao.model.dto.LoginResponseDTO;
import com.parcao.model.dto.SignupRequestDTO;
import com.parcao.model.enums.MensagemEnum;
import com.parcao.payload.response.MessageResponse;
import com.parcao.service.IAuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private static final String CORRELATION_ID = "test-correlation-id";

    @Mock
    private IAuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MDC.put("correlationId", CORRELATION_ID);
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldAuthenticateUserSuccessfully() {
        LoginRequestDTO request = new LoginRequestDTO("usuario.teste", "senha123");
        LoginResponseDTO expectedResponse = new LoginResponseDTO("jwt-token-gerado");
        when(authService.authenticate(request)).thenReturn(expectedResponse);

        ResponseEntity<LoginResponseDTO> response = authController.authenticateUser(request);
        LoginResponseDTO responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(expectedResponse.getToken(), responseBody.getToken());
        verify(authService, times(1)).authenticate(request);
        verifyNoMoreInteractions(authService);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        SignupRequestDTO request = buildSignupRequest();
        doNothing().when(authService).registerUser(request);

        ResponseEntity<Object> response = authController.registerUser(request);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(MensagemEnum.SUCESSO, response.getBody())
        );
        verify(authService, times(1)).registerUser(request);
        verifyNoMoreInteractions(authService);
    }

    @Test
    void shouldReturnConflictWhenUserAlreadyExistsDuringRegister() {
        SignupRequestDTO request = buildSignupRequest();
        doThrow(new UserAlreadyExistsException("Usuário já existe")).when(authService).registerUser(request);

        ResponseEntity<Object> response = authController.registerUser(request);

        assertAll(
                () -> assertEquals(HttpStatus.CONFLICT, response.getStatusCode()),
                () -> assertEquals(MensagemEnum.USUARIO_JA_EXISTE, response.getBody())
        );
        verify(authService, times(1)).registerUser(request);
        verifyNoMoreInteractions(authService);
    }

    @Test
    void shouldReturnBadRequestWhenRuntimeExceptionOccursDuringRegister() {
        SignupRequestDTO request = buildSignupRequest();
        String errorMessage = "Erro inesperado ao registrar usuário";
        doThrow(new RuntimeException(errorMessage)).when(authService).registerUser(request);

        ResponseEntity<Object> response = authController.registerUser(request);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals(errorMessage, response.getBody())
        );
        verify(authService, times(1)).registerUser(request);
        verifyNoMoreInteractions(authService);
    }

    @Test
    void shouldLogoutUserSuccessfullyAndReturnCleanCookie() {
        ResponseCookie cleanCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        when(authService.getCleanJwtCookie()).thenReturn(cleanCookie);

        ResponseEntity<Object> response = authController.logoutUser();
        Object responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cleanCookie.toString(), response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        assertInstanceOf(MessageResponse.class, responseBody);
        MessageResponse messageResponse = (MessageResponse) responseBody;
        assertNotNull(messageResponse);
        assertEquals(MensagemEnum.SUCESSO.getMensagem(), messageResponse.getMessage());
        verify(authService, times(1)).getCleanJwtCookie();
        verifyNoMoreInteractions(authService);
    }

    @Test
    void shouldAuthenticateUserEvenWhenCorrelationIdIsMissing() {
        MDC.remove("correlationId");
        LoginRequestDTO request = new LoginRequestDTO("outro.usuario", "senha456");
        LoginResponseDTO expectedResponse = new LoginResponseDTO("outro-token");
        when(authService.authenticate(request)).thenReturn(expectedResponse);

        ResponseEntity<LoginResponseDTO> response = authController.authenticateUser(request);
        LoginResponseDTO responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(expectedResponse.getToken(), responseBody.getToken());
        verify(authService, times(1)).authenticate(request);
        verifyNoMoreInteractions(authService);
    }

    private SignupRequestDTO buildSignupRequest() {
        return new SignupRequestDTO(
                1L,
                "usuario.teste",
                "Usuário Teste",
                "usuario@teste.com",
                Set.of("admin"),
                "senha123",
                Set.of("1")
        );
    }
}