package com.parcao.security.services;

import com.parcao.dto.ClienteDto;
import com.parcao.models.Cliente;
import com.parcao.repository.ClienteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Object save(ClienteDto clienteDto) {
        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(clienteDto, cliente);
        return clienteRepository.save(cliente);
    }

    @Override
    public Object findAll(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    @Override
    public boolean existsById(Long id) {
        return clienteRepository.existsById(id);
    }

}
