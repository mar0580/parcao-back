package com.parcao.service;

import com.parcao.dto.LoginRequestDTO;
import com.parcao.dto.LoginResponseDTO;
import com.parcao.dto.SignupRequestDTO;
import org.springframework.http.ResponseCookie;

public interface IAuthService {
    LoginResponseDTO authenticate(LoginRequestDTO request);
    void registerUser(SignupRequestDTO signUpRequest);
    ResponseCookie getCleanJwtCookie();
}