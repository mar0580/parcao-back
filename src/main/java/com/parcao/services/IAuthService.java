package com.parcao.services;

import com.parcao.exception.InvalidPasswordException;
import com.parcao.exception.UserNotFoundException;
import com.parcao.model.dto.ChangePasswordRequestDTO;
import com.parcao.model.dto.LoginRequestDTO;
import com.parcao.model.dto.LoginResponseDTO;
import com.parcao.model.dto.SignupRequestDTO;
import com.parcao.payload.response.MessageResponse;
import org.springframework.http.ResponseCookie;

public interface IAuthService {
    LoginResponseDTO authenticate(LoginRequestDTO request);
    void registerUser(SignupRequestDTO signUpRequest);
    ResponseCookie getCleanJwtCookie();
}