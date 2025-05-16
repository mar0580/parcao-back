package com.parcao.controllers;

import javax.validation.Valid;

import com.parcao.exception.InvalidPasswordException;
import com.parcao.exception.UserAlreadyExistsException;
import com.parcao.exception.UserNotFoundException;
import com.parcao.model.dto.*;
import com.parcao.model.enums.MensagemEnum;
import com.parcao.payload.response.MessageResponse;
import com.parcao.services.IAuthService;
import com.parcao.services.IJwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {

	private final IAuthService authService;
	private final IJwtService jwtService;

	public AuthController(IAuthService authService, IJwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

	@PostMapping("/signin")
	public ResponseEntity<LoginResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO request) {
		return ResponseEntity.ok(authService.authenticate(request));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDTO signUpRequest) {
		authService.registerUser(signUpRequest);
		return ResponseEntity.ok(MensagemEnum.SUCESSO);
	}

	//TODO
//	@PostMapping("/signout")
//	public ResponseEntity<?> logoutUser() {
//		ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
//		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
//				.body(MensagemEnum.SUCESSO);
//	}


	@PostMapping("/changepassword")
	public ResponseEntity<?> changePasswordUser(@Valid @RequestBody ChangePasswordRequestDTO changePasswordRequest) {
		MessageResponse response = authService.changePassword(changePasswordRequest);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/changedatauser")
	public ResponseEntity<?> changeDataUser(@Valid @RequestBody SignupRequestDTO signupRequest) {
		MessageResponse response = authService.prepareUserUpdate(signupRequest);
		return ResponseEntity.ok(response);
	}
}
