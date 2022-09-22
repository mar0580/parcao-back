package com.parcao.security.services;

import com.parcao.models.Cliente;
import com.parcao.repository.ClienteRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService{

    final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public boolean existsByTelefone(String telefone) {
        return clienteRepository.existsByTelefone(telefone);
    }

    @Override
    public Object findAll(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    @Override
    public boolean existsById(Long id) { return clienteRepository.existsById(id); }

    @Override
    public void deleleById(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public Optional<Cliente> findById(Long id) { return clienteRepository.findById(id); }

    @Override
    public Cliente save(Cliente cliente) { return clienteRepository.save(cliente); }
}
