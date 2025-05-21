package com.parcao.services.impl;

import com.parcao.exception.InvalidPasswordException;
import com.parcao.exception.UserAlreadyExistsException;
import com.parcao.model.dto.ChangePasswordRequestDTO;
import com.parcao.model.dto.LoginRequestDTO;
import com.parcao.model.dto.LoginResponseDTO;
import com.parcao.model.dto.SignupRequestDTO;
import com.parcao.model.entity.Filial;
import com.parcao.model.entity.Role;
import com.parcao.model.entity.Usuario;
import com.parcao.model.enums.ERole;
import com.parcao.model.enums.MensagemEnum;
import com.parcao.repository.RoleRepository;
import com.parcao.repository.UserRepository;
import com.parcao.services.IAuthService;
import com.parcao.services.IFilialService;
import com.parcao.services.IJwtService;
import lombok.RequiredArgsConstructor;
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

    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;
    private final IFilialService filialService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

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

        Set<Filial> filiais = mapFiliais(signUpRequest.getFilial());
        Set<Role> roles = mapRoles(signUpRequest.getRole());

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
    public ResponseCookie getCleanJwtCookie() {
        // Gera um cookie limpo para logout (ajuste conforme sua implementação de JWT)
        return ResponseCookie.from("luismarcelo", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .build();
    }

    private Set<Filial> mapFiliais(Set<String> strFiliais) {
        Set<Filial> filiais = new HashSet<>();
        strFiliais.forEach(nomeLocal -> {
            Filial filial = filialService.findByNomeLocal(nomeLocal)
                    .orElseThrow(() -> new RuntimeException(MensagemEnum.FILIAL_INEXISTENTE.toString()));
            filiais.add(filial);
        });
        return filiais;
    }

    private Set<Role> mapRoles(Set<String> strRoles) {
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
}