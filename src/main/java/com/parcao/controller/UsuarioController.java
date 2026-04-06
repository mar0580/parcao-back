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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UsuarioController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioController.class);
    private static final String CORRELATION_ID = "correlationId";

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Usuario> getAllUsers() {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para listar todos os usuários", correlationId);

        List<Usuario> usuarios = usuarioService.getAllUsers();

        LOGGER.info("[correlationId={}] Listagem de usuários concluída", correlationId);
        return usuarios;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<Usuario> getUserById(@PathVariable(value = "id") Long id) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para buscar usuário: {}", correlationId, id);

        Usuario usuario = usuarioService.getUserById(id);

        LOGGER.info("[correlationId={}] Usuário encontrado: {}", correlationId, id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/changepassword")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> changePasswordUser(@Valid @RequestBody ChangePasswordRequestDTO changePasswordRequest) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para alterar senha do usuário: {}", correlationId, changePasswordRequest.getUserName());

        try {
            MessageResponse response = usuarioService.changePassword(changePasswordRequest);
            LOGGER.info("[correlationId={}] Senha alterada com sucesso para usuário: {}", correlationId, changePasswordRequest.getUserName());
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException exception) {
            LOGGER.warn("[correlationId={}] Usuário não encontrado: {}", correlationId, changePasswordRequest.getUserName());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } catch (InvalidPasswordException exception) {
            LOGGER.warn("[correlationId={}] Senha incorreta para usuário: {}", correlationId, changePasswordRequest.getUserName());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exception.getMessage());
        } catch (IllegalArgumentException exception) {
            LOGGER.warn("[correlationId={}] Erro de validação: {}", correlationId, exception.getMessage());
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping("/deleteuser/{idUsuario}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "idUsuario") Long idUsuario) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para excluir usuário: {}", correlationId, idUsuario);

        try {
            usuarioService.deleteUser(idUsuario);
            LOGGER.info("[correlationId={}] Usuário excluído com sucesso: {}", correlationId, idUsuario);
            return ResponseEntity.ok(new MessageResponse(MensagemEnum.SUCESSO.getMensagem()));
        } catch (ResourceNotFoundException exception) {
            LOGGER.warn("[correlationId={}] Usuário não encontrado: {}", correlationId, idUsuario);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(MensagemEnum.USUARIO_NAO_EXISTE.getMensagem()));
        }
    }

    @PutMapping("/changedatauser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> changeDataUser(@Valid @RequestBody SignupRequestDTO signupRequest) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para atualizar dados do usuário: {}", correlationId, signupRequest.getId());

        try {
            usuarioService.updateUserData(signupRequest);
            LOGGER.info("[correlationId={}] Dados do usuário atualizados com sucesso: {}", correlationId, signupRequest.getId());
            return ResponseEntity.ok(new MessageResponse(MensagemEnum.SUCESSO.getMensagem()));
        } catch (ResourceNotFoundException exception) {
            LOGGER.warn("[correlationId={}] Usuário não encontrado: {}", correlationId, signupRequest.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MensagemEnum.USUARIO_NAO_EXISTE.getMensagem());
        }
    }

    private String currentCorrelationId() {
        return MDC.get(CORRELATION_ID);
    }
}
