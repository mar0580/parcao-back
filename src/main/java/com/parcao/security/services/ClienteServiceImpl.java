package com.parcao.security.services;

import com.parcao.dto.ClienteDto;
import com.parcao.models.Cliente;
import com.parcao.repository.ClienteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl implements ClienteService{

    final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public boolean existsByNomeCliente(String nomeCliente) {
        return false;
    }

    @Override
    public Object save(ClienteDto clienteDto) {
        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(clienteDto, cliente);
        return clienteRepository.save(cliente);
    }
}
