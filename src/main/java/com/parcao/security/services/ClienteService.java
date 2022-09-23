package com.parcao.security.services;

import com.parcao.dto.ClienteDto;
import com.parcao.models.Cliente;
import com.parcao.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    public boolean existsByTelefone(String telefone);

    List<Cliente> findAll();

    public boolean existsById(Long id);

    public void deleleById(Long id);

    public Optional<Cliente> findById(Long id);

    public Cliente save(Cliente cliente);
}
