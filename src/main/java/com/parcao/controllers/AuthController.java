package com.parcao.controllers;

import javax.validation.Valid;

import com.parcao.exception.UserAlreadyExistsException;
import com.parcao.model.dto.*;
import com.parcao.model.enums.MensagemEnum;
import com.parcao.payload.response.MessageResponse;
import com.parcao.services.IAuthService;
import com.parcao.services.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequiredArgsConstructor
public class AuthController {

	private final IAuthService authService;
	private final IJwtService jwtService;

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

	@PostMapping("/signout")
	public ResponseEntity<?> logoutUser() {
		ResponseCookie cookie = authService.getCleanJwtCookie();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body(new MessageResponse(MensagemEnum.SUCESSO.getMensagem()));
	}
}
