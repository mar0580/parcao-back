package com.parcao.service.impl;

import com.parcao.exception.UserAlreadyExistsException;
import com.parcao.dto.LoginRequestDTO;
import com.parcao.dto.LoginResponseDTO;
import com.parcao.dto.SignupRequestDTO;
import com.parcao.model.Filial;
import com.parcao.model.Role;
import com.parcao.model.Usuario;
import com.parcao.enums.ERole;
import com.parcao.enums.MensagemEnum;
import com.parcao.repository.RoleRepository;
import com.parcao.repository.UserRepository;
import com.parcao.service.IAuthService;
import com.parcao.service.IFilialService;
import com.parcao.service.IJwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final String CORRELATION_ID = "correlationId";

    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;
    private final IFilialService filialService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO request) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Autenticando usuário: {}", correlationId, request.getUserName());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(authentication);

        logger.info("[correlationId={}] Usuário autenticado com sucesso: {}", correlationId, request.getUserName());
        return new LoginResponseDTO(token);
    }

    @Override
    public void registerUser(SignupRequestDTO signUpRequest) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Registrando novo usuário: {}", correlationId, signUpRequest.getUserName());

        if (userRepository.existsByUserName(signUpRequest.getUserName()) ||
                userRepository.existsByEmail(signUpRequest.getEmail())) {
            logger.warn("[correlationId={}] Usuário ou email já existe: {}", correlationId, signUpRequest.getUserName());
            throw new UserAlreadyExistsException(MensagemEnum.USUARIO_JA_EXISTE.toString());
        }

        Set<Filial> filiais = mapFiliais(signUpRequest.getFilial(), correlationId);
        Set<Role> roles = mapRoles(signUpRequest.getRole(), correlationId);

        Usuario user = new Usuario(
                signUpRequest.getUserName(),
                signUpRequest.getNomeCompleto(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword())
        );

        user.setRoles(roles);
        user.setFiliais(filiais);
        userRepository.save(user);

        logger.info("[correlationId={}] Usuário registrado com sucesso: {}", correlationId, signUpRequest.getUserName());
    }

    @Override
    public ResponseCookie getCleanJwtCookie() {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Gerando cookie limpo para logout", correlationId);

        ResponseCookie cookie = ResponseCookie.from("parcao-jwt", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();

        logger.info("[correlationId={}] Cookie limpo gerado", correlationId);
        return cookie;
    }

    private Set<Filial> mapFiliais(Set<String> strFiliais, String correlationId) {
        logger.info("[correlationId={}] Mapeando filiais", correlationId);
        Set<Filial> filiais = new HashSet<>();
        if (strFiliais != null) {
            strFiliais.forEach(nomeLocal -> {
                Filial filial = filialService.findByNomeLocal(nomeLocal)
                        .orElseThrow(() -> new RuntimeException(MensagemEnum.FILIAL_INEXISTENTE.toString()));
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
                    .orElseThrow(() -> new RuntimeException(MensagemEnum.ROLE_INEXISTENTE.toString()));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                Role mappedRole = switch (role.toLowerCase()) {
                    case "admin" -> roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException(MensagemEnum.ROLE_INEXISTENTE.toString()));
                    case "mod" -> roleRepository.findByName(ERole.ROLE_MODERATOR)
                            .orElseThrow(() -> new RuntimeException(MensagemEnum.ROLE_INEXISTENTE.toString()));
                    default -> roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException(MensagemEnum.ROLE_INEXISTENTE.toString()));
                };
                roles.add(mappedRole);
            });
        }
        logger.info("[correlationId={}] {} roles mapeadas", correlationId, roles.size());
        return roles;
    }
}