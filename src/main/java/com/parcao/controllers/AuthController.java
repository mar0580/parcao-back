package com.parcao.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.parcao.payload.request.ChangePasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.parcao.models.ERole;
import com.parcao.models.Role;
import com.parcao.models.User;
import com.parcao.payload.request.LoginRequest;
import com.parcao.payload.request.SignupRequest;
import com.parcao.payload.response.MessageResponse;
import com.parcao.payload.response.UserInfoResponse;
import com.parcao.repository.RoleRepository;
import com.parcao.repository.UserRepository;
import com.parcao.security.jwt.JwtUtils;
import com.parcao.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
		
	@Value("${parcao.app.cargo.inexistente}")
	private String CARGO_INEXISTENTE_ERRO;
	
	@Value("${parcao.app.usuario.cadastrado}")
	private String CADASTRO_SUCESSO;
	
	@Value("${parcao.app.usuario.desconectado}")
	private String DESCONECTADO_SUCESSO;
	
	@Value("${parcao.app.usuario.atualizado}")
	private String ATUALIZADO;
	
	@Value("${parcao.app.usuario.inexistente}")
	private String USUARIO_INEXISTENTE;

	@Value("${parcao.app.usuario.senha.incorreta}")
	private String SENHA_ANTIGA_INCORRETA;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(
				new UserInfoResponse(userDetails.getId(), userDetails.getUserName(), userDetails.getEmail(), roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUserName(signUpRequest.getUserName())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Erro: Usuario já existe"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Erro: E-mail em uso"));
		}

		// Cria uma nova conta de usuario
		User user = new User(signUpRequest.getUserName(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException(CARGO_INEXISTENTE_ERRO));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException(CARGO_INEXISTENTE_ERRO));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException(CARGO_INEXISTENTE_ERRO));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException(CARGO_INEXISTENTE_ERRO));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse(CADASTRO_SUCESSO));
	}

	@PostMapping("/signout")
	public ResponseEntity<?> logoutUser() {
		ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body(new MessageResponse(DESCONECTADO_SUCESSO));
	}

	@PostMapping("/changepassword")
	public ResponseEntity<?> changePasswordUser(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
		if (!userRepository.existsByUserName(changePasswordRequest.getUserName())) {
			return ResponseEntity.badRequest().body(new MessageResponse(USUARIO_INEXISTENTE));
		}

		Optional<User> user = userRepository.findByUserName(changePasswordRequest.getUserName());
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

		boolean isPasswordMatches = bcrypt.matches(changePasswordRequest.getOldPassword(), user.get().getPassword());
		if(!isPasswordMatches){
			return ResponseEntity.badRequest().body(new MessageResponse(SENHA_ANTIGA_INCORRETA));
		}

		User userUpdate = new User();
		userUpdate.setId(user.get().getId());
		userUpdate.setUserName(user.get().getUserName());
		userUpdate.setEmail(user.get().getEmail());
		userUpdate.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
		userUpdate.setRoles(user.get().getRoles());		

		userRepository.save(userUpdate);

		return ResponseEntity.ok(new MessageResponse(ATUALIZADO));
	}
}
