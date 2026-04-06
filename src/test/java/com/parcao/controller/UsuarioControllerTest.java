package com.parcao.controller;

import com.parcao.exception.InvalidPasswordException;
import com.parcao.exception.ResourceNotFoundException;
import com.parcao.exception.UserNotFoundException;
import com.parcao.dto.ChangePasswordRequestDTO;
import com.parcao.dto.SignupRequestDTO;
import com.parcao.model.Usuario;
import com.parcao.enums.MensagemEnum;
import com.parcao.payload.response.MessageResponse;
import com.parcao.service.IUsuarioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    private static final String CORRELATION_ID = "test-correlation-id";

    @Mock
    private IUsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MDC.put("correlationId", CORRELATION_ID);
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldReturnAllUsers() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUserName("usuario.teste");
        List<Usuario> expectedUsers = List.of(usuario);
        when(usuarioService.getAllUsers()).thenReturn(expectedUsers);

        List<Usuario> response = usuarioController.getAllUsers();

        assertEquals(expectedUsers, response);
        verify(usuarioService, times(1)).getAllUsers();
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void shouldReturnUserById() {
        Long userId = 1L;
        Usuario expectedUser = new Usuario();
        expectedUser.setId(userId);
        when(usuarioService.getUserById(userId)).thenReturn(expectedUser);

        ResponseEntity<Usuario> response = usuarioController.getUserById(userId);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expectedUser, response.getBody())
        );
        verify(usuarioService, times(1)).getUserById(userId);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("usuario.teste", "senhaAntiga", "senhaNova");
        MessageResponse expectedResponse = new MessageResponse(MensagemEnum.SENHA_ALTERADA_COM_SUCESSO.getMensagem());
        when(usuarioService.changePassword(request)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = usuarioController.changePasswordUser(request);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expectedResponse, response.getBody())
        );
        verify(usuarioService, times(1)).changePassword(request);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void shouldReturnNotFoundWhenChangingPasswordForUnknownUser() {
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("inexistente", "senhaAntiga", "senhaNova");
        String errorMessage = "Usuário não encontrado";
        when(usuarioService.changePassword(request)).thenThrow(new UserNotFoundException(errorMessage));

        ResponseEntity<Object> response = usuarioController.changePasswordUser(request);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(errorMessage, response.getBody())
        );
        verify(usuarioService, times(1)).changePassword(request);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void shouldReturnNotAcceptableWhenChangingPasswordWithInvalidCurrentPassword() {
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("usuario.teste", "senhaErrada", "senhaNova");
        String errorMessage = "Senha incorreta";
        when(usuarioService.changePassword(request)).thenThrow(new InvalidPasswordException(errorMessage));

        ResponseEntity<Object> response = usuarioController.changePasswordUser(request);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode()),
                () -> assertEquals(errorMessage, response.getBody())
        );
        verify(usuarioService, times(1)).changePassword(request);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void shouldReturnBadRequestWhenChangingPasswordWithInvalidArguments() {
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("usuario.teste", "", "nova");
        String errorMessage = "Dados inválidos";
        when(usuarioService.changePassword(request)).thenThrow(new IllegalArgumentException(errorMessage));

        ResponseEntity<Object> response = usuarioController.changePasswordUser(request);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals(errorMessage, response.getBody())
        );
        verify(usuarioService, times(1)).changePassword(request);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        Long userId = 1L;
        doNothing().when(usuarioService).deleteUser(userId);

        ResponseEntity<Object> response = usuarioController.deleteUser(userId);
        Object responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(MessageResponse.class, responseBody);
        MessageResponse messageResponse = (MessageResponse) responseBody;
        assertNotNull(messageResponse);
        assertEquals(MensagemEnum.SUCESSO.getMensagem(), messageResponse.getMessage());
        verify(usuarioService, times(1)).deleteUser(userId);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingUnknownUser() {
        Long userId = 99L;
        doThrow(new ResourceNotFoundException("Usuário não existe")).when(usuarioService).deleteUser(userId);

        ResponseEntity<Object> response = usuarioController.deleteUser(userId);
        Object responseBody = response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertInstanceOf(MessageResponse.class, responseBody);
        MessageResponse messageResponse = (MessageResponse) responseBody;
        assertNotNull(messageResponse);
        assertEquals(MensagemEnum.USUARIO_NAO_EXISTE.getMensagem(), messageResponse.getMessage());
        verify(usuarioService, times(1)).deleteUser(userId);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void shouldChangeUserDataSuccessfully() {
        SignupRequestDTO request = buildSignupRequest();
        when(usuarioService.updateUserData(request)).thenReturn(new Usuario());

        ResponseEntity<Object> response = usuarioController.changeDataUser(request);
        Object responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(MessageResponse.class, responseBody);
        MessageResponse messageResponse = (MessageResponse) responseBody;
        assertNotNull(messageResponse);
        assertEquals(MensagemEnum.SUCESSO.getMensagem(), messageResponse.getMessage());
        verify(usuarioService, times(1)).updateUserData(request);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void shouldReturnNotFoundWhenChangingUserDataForUnknownUser() {
        SignupRequestDTO request = buildSignupRequest();
        when(usuarioService.updateUserData(request)).thenThrow(new ResourceNotFoundException("Usuário não existe"));

        ResponseEntity<Object> response = usuarioController.changeDataUser(request);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(MensagemEnum.USUARIO_NAO_EXISTE.getMensagem(), response.getBody())
        );
        verify(usuarioService, times(1)).updateUserData(request);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void shouldGetAllUsersEvenWhenCorrelationIdIsMissing() {
        MDC.remove("correlationId");
        when(usuarioService.getAllUsers()).thenReturn(List.of());

        List<Usuario> response = usuarioController.getAllUsers();

        assertNotNull(response);
        assertEquals(0, response.size());
        verify(usuarioService, times(1)).getAllUsers();
        verifyNoMoreInteractions(usuarioService);
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