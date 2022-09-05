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
		
	@Value("parcao.app.retorno.role_not_exists")
	private String INEXISTENTE;
	
	@Value("${parcao.app.retorno.success}")
	private String SUCESSO;		

	@Value("${parcao.app.retorno.error}")
	private String ERRO;
	
	@Value("${parcao.app.retorno.user_already_exists}")
	private String USUARIO_JA_EXISTE;

	@Value("${parcao.app.retorno.new_password_incorrect}")
	private String NEW_PASSWORD_INCORRECT;

	@Value("${parcao.app.retorno.user_not_exists}")
	private String USUARIO_NAO_EXISTE;

	@Value("${parcao.app.retorno.email_already_exists}")
	private String EMAIL_JA_EXISTE;

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
			return ResponseEntity.badRequest().body(new MessageResponse(USUARIO_JA_EXISTE));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse(EMAIL_JA_EXISTE));
		}

		// Cria uma nova conta de usuario
		User user = new User(signUpRequest.getUserName(), signUpRequest.getNomeCompleto() ,signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException(INEXISTENTE));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException(INEXISTENTE));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException(INEXISTENTE));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException(INEXISTENTE));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse(SUCESSO));
	}

	@PostMapping("/signout")
	public ResponseEntity<?> logoutUser() {
		ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body(new MessageResponse(SUCESSO));
	}

	@PostMapping("/changepassword")
	public ResponseEntity<?> changePasswordUser(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
		if (!userRepository.existsByUserName(changePasswordRequest.getUserName())) {
			return ResponseEntity.badRequest().body(new MessageResponse(USUARIO_NAO_EXISTE));
		}

		Optional<User> user = userRepository.findByUserName(changePasswordRequest.getUserName());
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

		boolean isPasswordMatches = bcrypt.matches(changePasswordRequest.getOldPassword(), user.get().getPassword());
		if(!isPasswordMatches){
			return ResponseEntity.badRequest().body(new MessageResponse(NEW_PASSWORD_INCORRECT));
		}

		User userUpdate = new User();
		userUpdate.setId(user.get().getId());
		userUpdate.setNomeCompleto(user.get().getNomeCompleto());
		userUpdate.setUserName(user.get().getUserName());
		userUpdate.setEmail(user.get().getEmail());
		userUpdate.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
		userUpdate.setRoles(user.get().getRoles());		

		userRepository.save(userUpdate);

		return ResponseEntity.ok(new MessageResponse(SUCESSO));
	}
}
