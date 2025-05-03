package com.parcao.services.impl;

import com.parcao.model.entity.Usuario;
import com.parcao.repository.UserRepository;
import com.parcao.services.UsuarioService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UserRepository usuarioRepository;

    public UsuarioServiceImpl(UserRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> getAllUsers() {
        return new ArrayList<>(usuarioRepository.findAll(Sort.by(Sort.Direction.ASC, "nomeCompleto")));
    }
}
