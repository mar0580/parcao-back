package com.parcao.services.impl;

import com.parcao.exception.InvalidPasswordException;
import com.parcao.exception.UserAlreadyExistsException;
import com.parcao.exception.UserNotFoundException;
import com.parcao.model.dto.*;
import com.parcao.model.entity.Filial;
import com.parcao.model.entity.Role;
import com.parcao.model.entity.Usuario;
import com.parcao.model.enums.ERole;
import com.parcao.model.enums.MensagemEnum;
import com.parcao.model.mapper.UsuarioMapper;
import com.parcao.payload.response.MessageResponse;
import com.parcao.repository.RoleRepository;
import com.parcao.repository.UserRepository;
import com.parcao.services.IAuthService;
import com.parcao.services.IFilialService;
import com.parcao.services.IJwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;
    private final IFilialService filialService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager, IJwtService jwtService,
                           UserRepository userRepository, IFilialService filialService, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.filialService = filialService;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(authentication);
        return new LoginResponseDTO(token);
    }

    @Override
    public void registerUser(SignupRequestDTO signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUserName()) ||
                userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistsException(MensagemEnum.USUARIO_JA_EXISTE.toString());
        }

        Set<Filial> filiais = prepareFiliais(signUpRequest.getFilial());
        Set<Role> roles = prepareRoles(signUpRequest.getRole());

        Usuario user = new Usuario(
                signUpRequest.getUserName(),
                signUpRequest.getNomeCompleto(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword())
        );

        user.setRoles(roles);
        user.setFiliais(filiais);
        userRepository.save(user);
    }

    @Override
    public MessageResponse changePassword(ChangePasswordRequestDTO request) throws UserNotFoundException, InvalidPasswordException {
        validateRequest(request);
        Usuario user = userRepository.findByUserName(request.getUserName()).orElseThrow(() -> new UserNotFoundException(MensagemEnum.USUARIO_NAO_EXISTE.getMensagem()));
        validateOldPassword(user, request.getOldPassword());
        updateUserPassword(user, request.getNewPassword());
        return new MessageResponse(MensagemEnum.SENHA_ALTERADA_COM_SUCESSO.getMensagem());
    }

    private void validateRequest(ChangePasswordRequestDTO request) {
        if (request.getNewPassword() == null || request.getNewPassword().isEmpty() ||
                request.getOldPassword() == null || request.getOldPassword().isEmpty()) {
            throw new IllegalArgumentException(MensagemEnum.SENHAS_VAZIAS.getMensagem());
        }
    }

    private void validateOldPassword(Usuario user, String oldPassword) {
        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidPasswordException(MensagemEnum.SENHA_INCORRETA.getMensagem());
        }
    }

    private void updateUserPassword(Usuario user, String newPassword) {
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    private Set<Filial> prepareFiliais(Set<String> strFiliais) {
        Set<Filial> filiais = new HashSet<>();
        strFiliais.forEach(nomeLocal -> {
            Filial filial = filialService.findByNomeLocal(nomeLocal)
                    .orElseThrow(() -> new RuntimeException(MensagemEnum.FILIAL_INEXISTENTE.toString()));
            filiais.add(filial);
        });
        return filiais;
    }

    private Set<Role> prepareRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(MensagemEnum.ROLE_INEXISTENTE.toString()));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                Role mappedRole = switch (role) {
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
        return roles;
    }

    public MessageResponse prepareUserUpdate(SignupRequestDTO signupRequest) {

        Optional<Usuario> existingUser = userRepository.findById(signupRequest.getId());
        UsuarioDTO userUpdate = new UsuarioDTO();

        userUpdate.setId(signupRequest.getId());
        userUpdate.setNomeCompleto(signupRequest.getNomeCompleto());
        userUpdate.setUserName(existingUser.map(Usuario::getUserName).orElse(null));
        userUpdate.setEmail(signupRequest.getEmail());
        userUpdate.setPassword(existingUser.map(Usuario::getPassword).orElse(null));

        userUpdate.setFilial(prepareFiliais(signupRequest.getFilial()));
        userUpdate.setRole(prepareRoles(signupRequest.getRole()));

        Usuario usuario = UsuarioMapper.toEntity(userUpdate);
        userRepository.save(usuario);

        return new MessageResponse(MensagemEnum.SUCESSO.getMensagem());
    }
}