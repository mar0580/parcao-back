package com.parcao.model.mapper;

import com.parcao.model.dto.SignupRequestDTO;
import com.parcao.model.dto.UsuarioDTO;
import com.parcao.model.entity.Usuario;

public class UsuarioMapper {
    public static Usuario toEntity(SignupRequestDTO signupRequestDTO) {
        Usuario usuario = new Usuario();
        usuario.setUserName(signupRequestDTO.getUserName());
        usuario.setEmail(signupRequestDTO.getEmail());
        usuario.setPassword(signupRequestDTO.getPassword());
        return usuario;
    }

    public static SignupRequestDTO toDto(Usuario usuario) {
        SignupRequestDTO signupRequestDTO = new SignupRequestDTO();
        signupRequestDTO.setUserName(usuario.getUserName());
        signupRequestDTO.setEmail(usuario.getEmail());
        signupRequestDTO.setPassword(usuario.getPassword());
        return signupRequestDTO;
    }

    public static UsuarioDTO tousuarioDto(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUserName(usuario.getUserName());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setPassword(usuario.getPassword());
        return usuarioDTO;
    }

    public static Usuario usuarioDtoToEntity(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setUserName(usuarioDTO.getUserName());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(usuarioDTO.getPassword());
        return usuario;
    }
}
