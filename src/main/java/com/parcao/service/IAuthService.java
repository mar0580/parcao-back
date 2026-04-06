package com.parcao.service;

import com.parcao.model.dto.LoginRequestDTO;
import com.parcao.model.dto.LoginResponseDTO;
import com.parcao.model.dto.SignupRequestDTO;
import org.springframework.http.ResponseCookie;

public interface IAuthService {
    LoginResponseDTO authenticate(LoginRequestDTO request);
    void registerUser(SignupRequestDTO signUpRequest);
    ResponseCookie getCleanJwtCookie();
}