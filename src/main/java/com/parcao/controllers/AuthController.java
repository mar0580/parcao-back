package com.parcao.controllers;

import javax.validation.Valid;

import com.parcao.exception.UserAlreadyExistsException;
import com.parcao.model.dto.LoginResponseDTO;
import com.parcao.model.dto.SignupRequestDTO;
import com.parcao.model.enums.MensagemEnum;
import com.parcao.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.parcao.model.dto.LoginRequestDTO;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
        this.authService = authService;
    }

	@PostMapping("/signin")
	public ResponseEntity<LoginResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO request) {
		return ResponseEntity.ok(authService.authenticate(request));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDTO signUpRequest) {
		try {
			authService.registerUser(signUpRequest);
			return ResponseEntity.ok(MensagemEnum.SUCESSO);
		} catch (UserAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(MensagemEnum.USUARIO_JA_EXISTE);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}


/*
	@PostMapping("/signout")
	public ResponseEntity<?> logoutUser() {
		ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body(new MessageResponse("SUCESSO"));
	}

	@PostMapping("/changepassword")
	public ResponseEntity<?> changePasswordUser(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
		if (!userRepository.existsByUserName(changePasswordRequest.getUserName()) ||
				((changePasswordRequest.getNewPassword().isEmpty() || changePasswordRequest.getOldPassword().isEmpty())) ) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USUARIO_NAO_EXISTE");
		}

		Optional<User> user = userRepository.findByUserName(changePasswordRequest.getUserName());
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

		boolean isPasswordMatches = bcrypt.matches(changePasswordRequest.getOldPassword(), user.get().getPassword());
		if(!isPasswordMatches){
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("PASSWORD_INCORRETO");
		}

		User userUpdate = new User();
		userUpdate.setId(user.get().getId());
		userUpdate.setNomeCompleto(user.get().getNomeCompleto());
		userUpdate.setUserName(user.get().getUserName());
		userUpdate.setEmail(user.get().getEmail());
		userUpdate.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
		userUpdate.setRoles(user.get().getRoles());

		userRepository.save(userUpdate);

		return ResponseEntity.ok(new MessageResponse("SUCESSO"));
	}



	@PutMapping("/changedatauser")
	public ResponseEntity<?> changeDataUser(@Valid @RequestBody SignupRequest signupRequest) {
		if (!userRepository.existsById(signupRequest.getId())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USUARIO_NAO_EXISTE");
		} else {
			Optional<User> user = userRepository.findById(signupRequest.getId());
			User userUpdate = new User();
			userUpdate.setId(signupRequest.getId());
			userUpdate.setNomeCompleto(signupRequest.getNomeCompleto());
			userUpdate.setUserName(user.get().getUserName());
			userUpdate.setEmail(signupRequest.getEmail());
			userUpdate.setPassword(user.get().getPassword());
			userUpdate.setDateInsert(LocalDateTime.now());

			Set<String> strFiliais = signupRequest.getFilial();
			Set<Filial> filiais = new HashSet<>();
			strFiliais.forEach(nomeLocal -> {
				Filial filial = filialService.findByNomeLocal(nomeLocal).orElseThrow(() -> new RuntimeException("INEXISTENTE"));
				filiais.add(filial);
			});

			Set<String> strRoles = signupRequest.getRole();
			Set<Role> roles = new HashSet<>();

			if (strRoles == null) {
				Role userRole = roleRepository.findByName(ERole.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("INEXISTENTE"));
				roles.add(userRole);
			} else {
				strRoles.forEach(role -> {
					switch (role) {
						case "admin":
							Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
									.orElseThrow(() -> new RuntimeException("INEXISTENTE"));
							roles.add(adminRole);

							break;
						case "mod":
							Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
									.orElseThrow(() -> new RuntimeException("INEXISTENTE"));
							roles.add(modRole);

							break;
						default:
							Role userRole = roleRepository.findByName(ERole.ROLE_USER)
									.orElseThrow(() -> new RuntimeException("INEXISTENTE"));
							roles.add(userRole);
					}
				});
			}

			userUpdate.setRoles(roles);
			userUpdate.setFiliais(filiais);

			userUpdate.setDateInsert(LocalDateTime.now());
			userRepository.save(userUpdate);
		}

		return ResponseEntity.ok(new MessageResponse("SUCESSO"));
	}

	*/
}
