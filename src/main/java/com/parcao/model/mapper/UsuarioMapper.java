package com.parcao.model.mapper;

import com.parcao.model.dto.UsuarioDTO;
import com.parcao.model.entity.Usuario;

public class UsuarioMapper {
    public static UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUserName(usuario.getUserName());
        dto.setNomeCompleto(usuario.getNomeCompleto());
        dto.setEmail(usuario.getEmail());
        dto.setRole(usuario.getRoles());
        dto.setFilial(usuario.getFiliais());
        return dto;
    }

    public static Usuario toEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setUserName(dto.getUserName());
        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setEmail(dto.getEmail());
        usuario.setRoles(dto.getRole());
        usuario.setFiliais(dto.getFilial());
        return usuario;
    }
}
