package com.parcao.service.impl;

import com.parcao.exception.InvalidPasswordException;
import com.parcao.exception.ResourceNotFoundException;
import com.parcao.exception.UserNotFoundException;
import com.parcao.model.dto.ChangePasswordRequestDTO;
import com.parcao.model.dto.SignupRequestDTO;
import com.parcao.model.entity.Filial;
import com.parcao.model.entity.Role;
import com.parcao.model.entity.Usuario;
import com.parcao.model.enums.ERole;
import com.parcao.model.enums.MensagemEnum;
import com.parcao.payload.response.MessageResponse;
import com.parcao.repository.RoleRepository;
import com.parcao.repository.UserRepository;
import com.parcao.service.IFilialService;
import com.parcao.service.IUsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);
    private static final String CORRELATION_ID = "correlationId";

    private final UserRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final IFilialService filialService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UserRepository usuarioRepository,
                               RoleRepository roleRepository,
                               IFilialService filialService,
                               PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.filialService = filialService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Usuario> getAllUsers() {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Buscando todos os usuários", correlationId);

        List<Usuario> usuarios = usuarioRepository.findAll(Sort.by(Sort.Direction.ASC, "nomeCompleto"));

        logger.info("[correlationId={}] {} usuários encontrados", correlationId, usuarios.size());
        return usuarios;
    }

    @Override
    public Usuario getUserById(Long id) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Buscando usuário por id: {}", correlationId, id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MensagemEnum.USUARIO_NAO_EXISTE.getMensagem()));

        logger.info("[correlationId={}] Usuário encontrado: {}", correlationId, usuario.getUserName());
        return usuario;
    }

    @Override
    @Transactional
    public MessageResponse changePassword(ChangePasswordRequestDTO changePasswordRequest) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Alterando senha do usuário: {}", correlationId, changePasswordRequest.getUserName());

        if (changePasswordRequest.getOldPassword() == null || changePasswordRequest.getNewPassword() == null) {
            logger.warn("[correlationId={}] Senhas não podem ser vazias", correlationId);
            throw new IllegalArgumentException(MensagemEnum.SENHAS_VAZIAS.getMensagem());
        }

        Usuario usuario = usuarioRepository.findByUserName(changePasswordRequest.getUserName())
                .orElseThrow(() -> {
                    logger.warn("[correlationId={}] Usuário não encontrado: {}", correlationId, changePasswordRequest.getUserName());
                    return new UserNotFoundException(MensagemEnum.USUARIO_NAO_EXISTE.getMensagem());
                });

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), usuario.getPassword())) {
            logger.warn("[correlationId={}] Senha incorreta para usuário: {}", correlationId, changePasswordRequest.getUserName());
            throw new InvalidPasswordException(MensagemEnum.SENHA_INCORRETA.getMensagem());
        }

        usuario.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        usuarioRepository.save(usuario);

        logger.info("[correlationId={}] Senha alterada com sucesso para usuário: {}", correlationId, changePasswordRequest.getUserName());
        return new MessageResponse(MensagemEnum.SENHA_ALTERADA_COM_SUCESSO.getMensagem());
    }

    @Override
    @Transactional
    public void deleteUser(Long idUsuario) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Excluindo usuário: {}", correlationId, idUsuario);

        if (!usuarioRepository.existsById(idUsuario)) {
            logger.warn("[correlationId={}] Usuário não encontrado: {}", correlationId, idUsuario);
            throw new ResourceNotFoundException(MensagemEnum.USUARIO_NAO_EXISTE.getMensagem());
        }

        usuarioRepository.deleteById(idUsuario);
        logger.info("[correlationId={}] Usuário excluído com sucesso: {}", correlationId, idUsuario);
    }

    @Override
    @Transactional
    public Usuario updateUserData(SignupRequestDTO signupRequest) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Atualizando dados do usuário: {}", correlationId, signupRequest.getId());

        Usuario usuario = usuarioRepository.findById(signupRequest.getId())
                .orElseThrow(() -> {
                    logger.warn("[correlationId={}] Usuário não encontrado: {}", correlationId, signupRequest.getId());
                    return new ResourceNotFoundException(MensagemEnum.USUARIO_NAO_EXISTE.getMensagem());
                });

        usuario.setNomeCompleto(signupRequest.getNomeCompleto());
        usuario.setEmail(signupRequest.getEmail());

        Set<Filial> filiais = mapFiliais(signupRequest.getFilial(), correlationId);
        Set<Role> roles = mapRoles(signupRequest.getRole(), correlationId);

        usuario.setFiliais(filiais);
        usuario.setRoles(roles);

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);

        logger.info("[correlationId={}] Dados do usuário atualizados com sucesso: {}", correlationId, signupRequest.getId());
        return usuarioAtualizado;
    }

    private Set<Filial> mapFiliais(Set<String> strFiliais, String correlationId) {
        logger.info("[correlationId={}] Mapeando filiais", correlationId);
        Set<Filial> filiais = new HashSet<>();
        if (strFiliais != null) {
            strFiliais.forEach(nomeLocal -> {
                Filial filial = filialService.findByNomeLocal(nomeLocal)
                        .orElseThrow(() -> new RuntimeException(MensagemEnum.FILIAL_INEXISTENTE.getMensagem()));
                filiais.add(filial);
            });
        }
        logger.info("[correlationId={}] {} filiais mapeadas", correlationId, filiais.size());
        return filiais;
    }

    private Set<Role> mapRoles(Set<String> strRoles, String correlationId) {
        logger.info("[correlationId={}] Mapeando roles", correlationId);
        Set<Role> roles = new HashSet<>();
        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(MensagemEnum.ROLE_INEXISTENTE.getMensagem()));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                Role mappedRole = switch (role.toLowerCase()) {
                    case "admin" -> roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException(MensagemEnum.ROLE_INEXISTENTE.getMensagem()));
                    case "mod" -> roleRepository.findByName(ERole.ROLE_MODERATOR)
                            .orElseThrow(() -> new RuntimeException(MensagemEnum.ROLE_INEXISTENTE.getMensagem()));
                    default -> roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException(MensagemEnum.ROLE_INEXISTENTE.getMensagem()));
                };
                roles.add(mappedRole);
            });
        }
        logger.info("[correlationId={}] {} roles mapeadas", correlationId, roles.size());
        return roles;
    }
}
