package com.parcao.services;

import com.parcao.exception.InvalidPasswordException;
import com.parcao.exception.UserNotFoundException;
import com.parcao.model.dto.*;
import com.parcao.payload.response.MessageResponse;

public interface IAuthService {
    LoginResponseDTO authenticate(LoginRequestDTO request);
    void registerUser(SignupRequestDTO signUpRequest);
    MessageResponse changePassword(ChangePasswordRequestDTO request)
            throws UserNotFoundException, InvalidPasswordException;
    MessageResponse prepareUserUpdate(SignupRequestDTO signUpRequest);
}