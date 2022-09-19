package com.parcao.security.services;

import com.parcao.dto.ClienteDto;
import com.parcao.models.Cliente;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClienteService {

    public boolean existsByTelefone(String telefone);

    public Object save(ClienteDto clienteDto);

    public Object findAll(Pageable pageable);

    public boolean existsById(Long id);

    public void deleleById(Long id);
}
