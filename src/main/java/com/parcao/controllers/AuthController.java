package com.parcao.controllers;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.parcao.model.dto.ChangePasswordRequest;
import com.parcao.model.entity.Filial;
import com.parcao.services.FilialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.parcao.model.enums.ERole;
import com.parcao.model.entity.Role;
import com.parcao.model.entity.User;
import com.parcao.model.dto.LoginRequest;
import com.parcao.model.dto.SignupRequest;
import com.parcao.payload.response.MessageResponse;
import com.parcao.payload.response.UserInfoResponse;
import com.parcao.repository.RoleRepository;
import com.parcao.repository.UserRepository;
import com.parcao.security.jwt.JwtUtils;
import com.parcao.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	final FilialService filialService;

	public AuthController(FilialService filialService) {
		this.filialService = filialService;
	}

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

		Optional<User> user = userRepository.findById(userDetails.getId());
		User userUpdate = new User();

		List<Long> idFiliais = user.get().getFiliais().stream().map(filial -> filial.getId()).collect(Collectors.toList());
		//Long idFilial = user.get().getFiliais().getId();

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(
				new UserInfoResponse(userDetails.getId(), userDetails.getUserName(), userDetails.getEmail(), roles, idFiliais));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if ( (userRepository.existsByUserName(signUpRequest.getUserName())) ||
				(userRepository.existsByEmail(signUpRequest.getEmail()))) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("USUARIO_JA_EXISTE");
		}

		Set<String> strFiliais = signUpRequest.getFilial();
		Set<Filial> filiais = new HashSet<>();
		strFiliais.forEach(nomeLocal -> {
			Filial filial = filialService.findByNomeLocal(nomeLocal).orElseThrow(() -> new RuntimeException("INEXISTENTE"));
			filiais.add(filial);
		});

		// Cria uma nova conta de usuario
		User user = new User(signUpRequest.getUserName(), signUpRequest.getNomeCompleto() ,signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
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

		user.setRoles(roles);
		user.setFiliais(filiais);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("SUCESSO"));
	}

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

	@GetMapping("/user")
	public List<User> getAllUsers()
	{
		List<User> users = new ArrayList<User>();
		userRepository.findAll(Sort.by(Sort.Direction.ASC, "nomeCompleto")).forEach(users1 -> users.add(users1));
		return users;
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

	@DeleteMapping("/deleteuser/{idUsuario}")
	public ResponseEntity<?> deleteUser(@PathVariable (value = "idUsuario") Long idUsuario) {
		if (!userRepository.existsById(idUsuario)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("USUARIO_NAO_EXISTE"));
		}
		userRepository.deleteById(idUsuario);
		return ResponseEntity.ok(new MessageResponse("SUCESSO"));
	}
}
