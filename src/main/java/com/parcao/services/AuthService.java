package com.parcao.services;

import com.parcao.model.dto.LoginRequestDTO;
import com.parcao.model.dto.LoginResponseDTO;
import com.parcao.model.dto.SignupRequestDTO;

public interface AuthService {
    LoginResponseDTO authenticate(LoginRequestDTO request);
    void registerUser(SignupRequestDTO signUpRequest);
}