package com.parcao.service;

import com.parcao.dto.ChangePasswordRequestDTO;
import com.parcao.dto.SignupRequestDTO;
import com.parcao.model.Usuario;
import com.parcao.payload.response.MessageResponse;

import java.util.List;

public interface IUsuarioService {
    List<Usuario> getAllUsers();

    MessageResponse changePassword(ChangePasswordRequestDTO changePasswordRequest);

    void deleteUser(Long idUsuario);

    Usuario updateUserData(SignupRequestDTO signupRequest);

    Usuario getUserById(Long id);
}
