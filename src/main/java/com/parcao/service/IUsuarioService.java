package com.parcao.service;

import com.parcao.model.dto.ChangePasswordRequestDTO;
import com.parcao.model.dto.SignupRequestDTO;
import com.parcao.model.entity.Usuario;
import com.parcao.payload.response.MessageResponse;

import java.util.List;

public interface IUsuarioService {
    List<Usuario> getAllUsers();

    MessageResponse changePassword(ChangePasswordRequestDTO changePasswordRequest);

    void deleteUser(Long idUsuario);

    Usuario updateUserData(SignupRequestDTO signupRequest);

    Usuario getUserById(Long id);
}
